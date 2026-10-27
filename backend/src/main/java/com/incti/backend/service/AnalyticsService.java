package com.incti.backend.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.incti.backend.dto.EtLoteRequest;
import com.incti.backend.dto.LoteResponse;
import com.incti.backend.model.EstatisticaLote;
import com.incti.backend.model.UploadLote;
import com.incti.backend.repository.EstatisticaLoteRepository;
import com.incti.backend.repository.UploadLoteRepository;

@Service
public class AnalyticsService {

    private final UploadLoteRepository loteRepository;
    private final EstatisticaLoteRepository estatisticaRepository;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private final String pythonUrl;
    private final String pythonKey;

    public AnalyticsService(
            UploadLoteRepository loteRepository,
            EstatisticaLoteRepository estatisticaRepository,
            ObjectMapper objectMapper,
            @Value("${analytics.python.url:http://localhost:8000}") String pythonUrl,
            @Value("${analytics.python.key:}") String pythonKey) {
        this.loteRepository = loteRepository;
        this.estatisticaRepository = estatisticaRepository;
        this.objectMapper = objectMapper;
        this.pythonUrl = pythonUrl.replaceAll("/$", "");
        this.pythonKey = pythonKey;
        this.httpClient = HttpClient.newHttpClient();
    }

    public LoteResponse processar(EtLoteRequest request) {
        UploadLote lote = new UploadLote();
        lote.setStatus("AGUARDANDO_ETL");
        lote.setTotalLinhas(request.linhas().size());
        lote.setLinhasValidas(0);
        lote.setLinhasInvalidas(0);
        lote.setMensagem("Aguardando processamento pelo serviço Python.");
        lote.setCriadoEm(LocalDateTime.now());
        lote = loteRepository.save(lote);

        try {
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("loteId", lote.getId());
            payload.put("recebidas", request.linhas().size());
            payload.put("linhas", request.linhas());

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(pythonUrl + "/etl"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            objectMapper.writeValueAsString(payload)));

            if (!pythonKey.isBlank()) {
                requestBuilder.header("X-ETL-Key", pythonKey);
            }

            HttpResponse<String> response = httpClient.send(
                    requestBuilder.build(),
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException(
                        "Python respondeu HTTP " + response.statusCode());
            }

            UploadLote atualizado = loteRepository.findById(lote.getId())
                    .orElse(lote);
            return new LoteResponse(
                    atualizado.getId(),
                    atualizado.getStatus(),
                    atualizado.getMensagem());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            return marcarFalha(lote, "Processamento Python interrompido.");
        } catch (java.io.IOException | RuntimeException exception) {
            return marcarFalha(lote, "Não foi possível falar com o serviço Python: "
                    + exception.getMessage());
        }
    }

    public JsonNode dashboard(Long loteId) {
        EstatisticaLote estatistica = estatisticaRepository
                .findFirstByLoteIdOrderByCriadoEmDesc(loteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Nenhuma análise encontrada para este lote."));
        try {
            return objectMapper.readTree(estatistica.getResultado());
        } catch (JacksonException exception) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Resultado da análise inválido.",
                    exception);
        }
    }

    private LoteResponse marcarFalha(UploadLote lote, String mensagem) {
        lote.setStatus("ERRO");
        lote.setMensagem(mensagem);
        loteRepository.save(lote);
        throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, mensagem);
    }
}
