package com.incti.backend.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.incti.backend.repository.UsuarioRepository;
import com.incti.backend.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public SecurityFilter(
            TokenService tokenService,
            UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization == null ||
                !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);

        try {
            String email = tokenService.validarToken(token);

            var usuarioOpt = usuarioRepository.findByEmail(email);

            if (usuarioOpt.isPresent()) {
                var usuario = usuarioOpt.get();

                var autenticacao =
                    new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        List.of(new SimpleGrantedAuthority(
                            "ROLE_" + usuario.getPerfil().name()
                        ))
                    );

                SecurityContextHolder.getContext()
                    .setAuthentication(autenticacao);

                System.out.println("JWT válido para: " + email);
            } else {
                System.out.println(
                    "JWT válido, mas usuário não encontrado: " + email
                );
                SecurityContextHolder.clearContext();
            }

        } catch (JWTVerificationException | IllegalArgumentException e) {
            System.out.println(
                "Erro ao validar JWT: " + e.getMessage()
            );
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
