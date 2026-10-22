package com.incti.backend.service;

import com.incti.backend.model.Servico;
import com.incti.backend.repository.ServicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
    }

    public Optional<Servico> buscarPorId(Long id) {
        return servicoRepository.findById(id);
    }

    public Servico criar(Servico servico) {
        return servicoRepository.save(servico);
    }

    public Optional<Servico> atualizar(Long id, Servico servicoAtualizado) {
        return servicoRepository.findById(id)
                .map(servico -> {
                    servico.setNome(servicoAtualizado.getNome());
                    servico.setCategoria(servicoAtualizado.getCategoria());

                    return servicoRepository.save(servico);
                });
    }

    public boolean excluir(Long id) {
        if (!servicoRepository.existsById(id)) {
            return false;
        }

        servicoRepository.deleteById(id);
        return true;
    }
}