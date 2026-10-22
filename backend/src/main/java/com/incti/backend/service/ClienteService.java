package com.incti.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.incti.backend.model.Cliente;
import com.incti.backend.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente criar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> atualizar(Long id, Cliente clienteAtualizado) {

        return clienteRepository.findById(id)
                .map(cliente -> {

                    cliente.setNome(clienteAtualizado.getNome());
                    cliente.setEmail(clienteAtualizado.getEmail());
                    cliente.setConsultor(clienteAtualizado.getConsultor());

                    return clienteRepository.save(cliente);
                });
    }

    public boolean excluir(Long id) {

        if (!clienteRepository.existsById(id)) {
            return false;
        }

        clienteRepository.deleteById(id);
        return true;
    }
}