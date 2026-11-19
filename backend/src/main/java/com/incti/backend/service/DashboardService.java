package com.incti.backend.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.incti.backend.model.EstatisticaLote;
import com.incti.backend.model.UploadLote;
import com.incti.backend.repository.EstatisticaLoteRepository;
import com.incti.backend.repository.UploadLoteRepository;

@Service
public class DashboardService {

    private final UploadLoteRepository loteRepository;
    private final EstatisticaLoteRepository estatisticaRepository;
    private final ObjectMapper objectMapper;

    public DashboardService(
            UploadLoteRepository loteRepository,
            EstatisticaLoteRepository estatisticaRepository,
            ObjectMapper objectMapper) {
        this.loteRepository = loteRepository;
        this.estatisticaRepository = estatisticaRepository;
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> obter(Long loteId) {
        UploadLote lote = loteId == null
                ? loteRepository.findAll().stream()
                    .filter(item -> "PROCESSADO".equals(item.getStatus()))
                    .max(Comparator.comparing(UploadLote::getId))
                    .orElseThrow(() -> semDados())
                : loteRepository.findById(loteId)
                    .orElseThrow(() -> semDados());

        EstatisticaLote estatistica = estatisticaRepository
                .findFirstByLoteIdOrderByCriadoEmDesc(lote.getId())
                .orElseThrow(() -> semDados());

        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("lote", lote);
        resposta.put("geradoEm", estatistica.getCriadoEm());
        resposta.put("estatisticas", lerJson(estatistica.getResultado()));
        return resposta;
    }

    public List<Map<String, Object>> listarLotes() {
        return loteRepository.findAll().stream()
                .sorted(Comparator.comparing(UploadLote::getId).reversed())
                .limit(20)
                .map(this::resumoLote)
                .toList();
    }

    private Map<String, Object> resumoLote(UploadLote lote) {
        Map<String, Object> resumo = new LinkedHashMap<>();
        resumo.put("id", lote.getId());
        resumo.put("arquivoNome", lote.getArquivoNome());
        resumo.put("status", lote.getStatus());
        resumo.put("totalLinhas", lote.getTotalLinhas());
        resumo.put("linhasValidas", lote.getLinhasValidas());
        resumo.put("linhasInvalidas", lote.getLinhasInvalidas());
        resumo.put("criadoEm", lote.getCriadoEm());
        return resumo;
    }

    private JsonNode lerJson(String resultado) {
        try {
            return objectMapper.readTree(resultado);
        } catch (JacksonException exception) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Resultado da análise inválido.",
                    exception);
        }
    }

    private ResponseStatusException semDados() {
        return new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Nenhum lote processado encontrado.");
    }
}
