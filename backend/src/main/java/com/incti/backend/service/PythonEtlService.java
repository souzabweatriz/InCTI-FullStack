package com.incti.backend.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
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
public class PythonEtlService {

    private final UploadLoteRepository loteRepository;
    private final EstatisticaLoteRepository estatisticaRepository;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private final String pythonUrl;
    private final String pythonKey;

    public PythonEtlService(
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
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public LoteResponse processar(EtLoteRequest request) {
        UploadLote lote = new UploadLote();
        lote.setArquivoNome(request.arquivoNome() == null
                ? "planilha" : request.arquivoNome());
        lote.setStatus("AGUARDANDO_ETL");
        lote.setTotalLinhas(request.recebidas() == null
                ? request.linhas().size() : request.recebidas());
        lote.setLinhasValidas(0);
        lote.setLinhasInvalidas(request.invalidas() == null
                ? 0 : request.invalidas());
        lote.setMensagem("Aguardando processamento pelo serviço Python.");
        lote.setCriadoEm(LocalDateTime.now());
        lote = loteRepository.save(lote);

        try {
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("loteId", lote.getId());
            payload.put("arquivoNome", request.arquivoNome() == null
                    ? "planilha" : request.arquivoNome());
            payload.put("recebidas", request.recebidas() == null
                    ? request.linhas().size() : request.recebidas());
            payload.put("invalidas", request.invalidas() == null
                    ? 0 : request.invalidas());
            payload.put("comAviso", request.comAviso() == null
                    ? 0 : request.comAviso());
            payload.put("linhas", request.linhas());

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(pythonUrl + "/etl"))
                    .timeout(Duration.ofMinutes(2))
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
                return marcarFalha(lote, mensagemDoPython(response));
            }

            // 2xx do Python = lote gravado como PROCESSADO. (Não dá pra reler via
            // findById aqui: o JPA devolveria a cópia em cache, ainda AGUARDANDO_ETL.)
            return new LoteResponse(
                    lote.getId(),
                    "PROCESSADO",
                    "Análise concluída.");
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            return marcarFalha(lote, "Processamento Python interrompido.");
        } catch (java.net.ConnectException exception) {
            return marcarFalha(lote, "O serviço Python não está rodando em "
                    + pythonUrl + ". Inicie com: python -m uvicorn api:app --port 8000");
        } catch (java.net.http.HttpTimeoutException exception) {
            return marcarFalha(lote, "O serviço Python demorou demais para responder.");
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

    private String mensagemDoPython(HttpResponse<String> response) {
        try {
            JsonNode detalhe = objectMapper.readTree(response.body()).get("detail");
            if (detalhe != null && detalhe.isString()) {
                return detalhe.asString();
            }
        } catch (JacksonException ignored) {
            // corpo não é JSON: usa a mensagem genérica abaixo
        }
        return "Serviço Python respondeu HTTP " + response.statusCode() + ".";
    }

    private LoteResponse marcarFalha(UploadLote lote, String mensagem) {
        String texto = mensagem.length() > 255
                ? mensagem.substring(0, 255) : mensagem;
        // Recarrega: o Python pode já ter gravado o status ERRO com detalhes.
        UploadLote atual = loteRepository.findById(lote.getId()).orElse(lote);
        atual.setStatus("ERRO");
        atual.setMensagem(texto);
        loteRepository.save(atual);
        throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, texto);
    }
}
