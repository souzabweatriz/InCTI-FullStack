package com.incti.backend.dto;

public record LoginRequest(
        String email,
        String senha
) {
}