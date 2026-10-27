package com.incti.backend.dto;

public record LoteResponse(
    Long loteId,
    String status,
    String mensagem
) {
}
