package com.incti.backend.dto;

public record UsuarioResponse(
        Long id,
        String nomeCompleto,
        String email,
        String perfil
) {
}