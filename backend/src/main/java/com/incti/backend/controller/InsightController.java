package com.incti.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incti.backend.model.Insight;
import com.incti.backend.service.InsightService;

@RestController
@RequestMapping("/insights")
public class InsightController {

    private final InsightService insightService;

    public InsightController(InsightService insightService) {
        this.insightService = insightService;
    }

    @GetMapping
    public List<Insight> listarTodos() {
        return insightService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Insight> buscarPorId(@PathVariable Long id) {
        return insightService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Insight criar(@RequestBody Insight insight) {
        return insightService.criar(insight);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Insight> atualizar(
            @PathVariable Long id,
            @RequestBody Insight insightAtualizado) {

        return insightService.atualizar(id, insightAtualizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (insightService.excluir(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}