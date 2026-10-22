package com.incti.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.incti.backend.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiration-minutes}")
    private long expirationMinutes;

    public String gerarToken(Usuario usuario) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withSubject(usuario.getEmail())
                .withClaim("nome", usuario.getNomeCompleto())
                .withExpiresAt(
                        Instant.now().plusSeconds(expirationMinutes * 60)
                )
                .sign(algorithm);
    }

    public String validarToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getSubject();
    }
}
