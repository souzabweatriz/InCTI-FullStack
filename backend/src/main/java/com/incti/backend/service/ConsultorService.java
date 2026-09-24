package com.incti.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.incti.backend.model.Consultor;
import com.incti.backend.repository.ConsultorRepository;

@Service
public class ConsultorService {

    private final ConsultorRepository repository;

    public ConsultorService(ConsultorRepository repository) {
        this.repository = repository;
    }

    public Consultor criar(Consultor consultor) {
        consultor.setId(null);
        return repository.save(consultor);
    }

    public List<Consultor> listar() {
        return repository.findAll();
    }

    public Consultor buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Consultor não encontrado"));
    }

    public Consultor atualizar(Long id, Consultor dados) {
        Consultor consultor = buscarPorId(id);

        consultor.setNome(dados.getNome());
        consultor.setMatricula(dados.getMatricula());

        return repository.save(consultor);
    }

    public void excluir(Long id) {
        Consultor consultor = buscarPorId(id);
        repository.delete(consultor);
    }

    public Consultor login(String email, String senha) {
        Consultor consultor = repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Email ou senha inválidos"));

        if (!consultor.getSenha().equals(senha)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Email ou senha inválidos");
        }

        return consultor;
    }
}