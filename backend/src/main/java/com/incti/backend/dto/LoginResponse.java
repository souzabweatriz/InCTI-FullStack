package com.incti.backend.dto;

public record LoginResponse(
        String token,
        String nome
) {
}