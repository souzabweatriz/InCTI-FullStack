package com.incti.backend.dto;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotEmpty;

public record EtLoteRequest(
    @NotEmpty List<Map<String, Object>> linhas
) {
}
