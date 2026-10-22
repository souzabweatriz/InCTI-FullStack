package com.incti.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.incti.backend.model.Contrato;
import com.incti.backend.repository.ContratoRepository;

@Service
public class ContratoService {

    private final ContratoRepository contratoRepository;

    public ContratoService(ContratoRepository contratoRepository) {
        this.contratoRepository = contratoRepository;
    }

    public List<Contrato> listarTodos() {
        return contratoRepository.findAll();
    }

    public Optional<Contrato> buscarPorId(Long id) {
        return contratoRepository.findById(id);
    }

    public Contrato criar(Contrato contrato) {
        return contratoRepository.save(contrato);
    }

    public Optional<Contrato> atualizar(Long id, Contrato contratoAtualizado) {
        return contratoRepository.findById(id)
                .map(contrato -> {
                    contrato.setDataInicio(contratoAtualizado.getDataInicio());
                    contrato.setAtivo(contratoAtualizado.getAtivo());
                    contrato.setServico(contratoAtualizado.getServico());

                    return contratoRepository.save(contrato);
                });
    }

    public boolean excluir(Long id) {
        if (!contratoRepository.existsById(id)) {
            return false;
        }

        contratoRepository.deleteById(id);
        return true;
    }
}