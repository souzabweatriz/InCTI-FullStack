package com.incti.backend.controller;

import com.incti.backend.model.Contrato;
import com.incti.backend.service.ContratoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contratos")
public class ContratoController {

    private final ContratoService contratoService;

    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @GetMapping
    public List<Contrato> listarTodos() {
        return contratoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contrato> buscarPorId(@PathVariable Long id) {
        return contratoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Contrato criar(@RequestBody Contrato contrato) {
        return contratoService.criar(contrato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contrato> atualizar(
            @PathVariable Long id,
            @RequestBody Contrato contratoAtualizado) {

        return contratoService.atualizar(id, contratoAtualizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (contratoService.excluir(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}