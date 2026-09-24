package com.incti.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incti.backend.model.Consultor;
import com.incti.backend.service.ConsultorService;

@RestController
@RequestMapping("/consultores")
public class ConsultorController {

    private final ConsultorService service;

    public ConsultorController(ConsultorService service) {
        this.service = service;
    }

    // POST /consultores
    // Cadastrar consultor
    @PostMapping
    public Consultor criar(@RequestBody Consultor consultor) {
        return service.criar(consultor);
    }

    // POST /consultores/login
    // Login do consultor
    @PostMapping("/login")
    public Consultor login(@RequestBody Consultor consultor) {
        return service.login(
                consultor.getEmail(),
                consultor.getSenha()
        );
    }

    // GET /consultores
    // Listar todos os consultores
    @GetMapping
    public List<Consultor> listar() {
        return service.listar();
    }

    // GET /consultores/{id}
    // Buscar consultor pelo ID
    @GetMapping("/{id}")
    public Consultor buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // PUT /consultores/{id}
    // Alterar consultor
    @PutMapping("/{id}")
    public Consultor atualizar(
            @PathVariable Long id,
            @RequestBody Consultor consultor) {
        return service.atualizar(id, consultor);
    }

    // DELETE /consultores/{id}
    // Excluir consultor
    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}