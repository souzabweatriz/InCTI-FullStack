package com.incti.backend.controller;

import com.incti.backend.model.Telemetria;
import com.incti.backend.service.TelemetriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/telemetrias")
public class TelemetriaController {

    private final TelemetriaService telemetriaService;

    public TelemetriaController(TelemetriaService telemetriaService) {
        this.telemetriaService = telemetriaService;
    }

    @GetMapping
    public List<Telemetria> listarTodos() {
        return telemetriaService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Telemetria> buscarPorId(@PathVariable Long id) {
        return telemetriaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Telemetria criar(@RequestBody Telemetria telemetria) {
        return telemetriaService.criar(telemetria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Telemetria> atualizar(
            @PathVariable Long id,
            @RequestBody Telemetria telemetriaAtualizada) {

        return telemetriaService.atualizar(id, telemetriaAtualizada)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (telemetriaService.excluir(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}