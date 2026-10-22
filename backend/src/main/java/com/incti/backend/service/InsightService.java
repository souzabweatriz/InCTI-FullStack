package com.incti.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.incti.backend.model.Insight;
import com.incti.backend.repository.InsightRepository;

@Service
public class InsightService {

    private final InsightRepository insightRepository;

    public InsightService(InsightRepository insightRepository) {
        this.insightRepository = insightRepository;
    }

    public List<Insight> listarTodos() {
        return insightRepository.findAll();
    }

    public Optional<Insight> buscarPorId(Long id) {
        return insightRepository.findById(id);
    }

    public Insight criar(Insight insight) {
        return insightRepository.save(insight);
    }

    public Optional<Insight> atualizar(Long id, Insight insightAtualizado) {
        return insightRepository.findById(id)
                .map(insight -> {
                    insight.setTipo(insightAtualizado.getTipo());
                    insight.setDescricao(insightAtualizado.getDescricao());

                    return insightRepository.save(insight);
                });
    }

    public boolean excluir(Long id) {
        if (!insightRepository.existsById(id)) {
            return false;
        }

        insightRepository.deleteById(id);
        return true;
    }
}