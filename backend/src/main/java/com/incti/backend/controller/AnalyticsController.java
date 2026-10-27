package com.incti.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tools.jackson.databind.JsonNode;
import com.incti.backend.dto.EtLoteRequest;
import com.incti.backend.dto.LoteResponse;
import com.incti.backend.service.AnalyticsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @PostMapping("/lotes")
    public ResponseEntity<LoteResponse> processar(
            @Valid @RequestBody EtLoteRequest request) {
        return ResponseEntity.ok(analyticsService.processar(request));
    }

    @GetMapping("/dashboard")
    public JsonNode dashboard(@RequestParam Long loteId) {
        return analyticsService.dashboard(loteId);
    }
}
