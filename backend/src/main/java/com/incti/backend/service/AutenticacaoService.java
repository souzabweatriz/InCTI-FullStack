package com.incti.backend.service;

import com.incti.backend.model.Usuario;
import com.incti.backend.repository.UsuarioRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AutenticacaoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Usuario autenticar(String email, String senha) {
        String emailNormalizado = email == null ? "" : email.trim();
        Usuario usuario = usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new BadCredentialsException("E-mail ou senha inválidos"));

        if (senha == null || !passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new BadCredentialsException("E-mail ou senha inválidos");
        }

        return usuario;
    }
}