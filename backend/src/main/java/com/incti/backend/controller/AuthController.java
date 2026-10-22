package com.incti.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incti.backend.dto.LoginRequest;
import com.incti.backend.dto.LoginResponse;
import com.incti.backend.dto.RegisterRequest;
import com.incti.backend.dto.UsuarioResponse;
import com.incti.backend.model.Usuario;
import com.incti.backend.service.AutenticacaoService;
import com.incti.backend.service.TokenService;
import com.incti.backend.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AutenticacaoService autenticacaoService;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    public AuthController(
            AutenticacaoService autenticacaoService,
            TokenService tokenService,
            UsuarioService usuarioService) {

        this.autenticacaoService = autenticacaoService;
        this.tokenService = tokenService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {

        Usuario usuario = autenticacaoService.autenticar(
            request.email(),
            request.senha()
        );

        return new LoginResponse(
            tokenService.gerarToken(usuario),
            usuario.getNomeCompleto()
        );
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> registrar(
            @Valid @RequestBody RegisterRequest request) {

        Usuario usuario = usuarioService.cadastrar(
            request.nomeCompleto(),
            request.email(),
            request.senha()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
            new UsuarioResponse(
                usuario.getId(),
                usuario.getNomeCompleto(),
                usuario.getEmail(),
                usuario.getPerfil().name()
            )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> me(
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Usuario usuario;
        if (authentication.getPrincipal() instanceof Usuario usuarioAutenticado) {
            usuario = usuarioAutenticado;
        } else {
            usuario = usuarioService.buscarPorEmail(authentication.getName());
        }

        UsuarioResponse resposta = new UsuarioResponse(
            usuario.getId(),
            usuario.getNomeCompleto(),
            usuario.getEmail(),
            usuario.getPerfil().name()
        );

        return ResponseEntity.ok(resposta);
    }
}
