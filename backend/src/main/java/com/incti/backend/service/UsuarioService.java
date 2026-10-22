package com.incti.backend.service;

import com.incti.backend.model.Perfil;
import com.incti.backend.model.Usuario;
import com.incti.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Usuario cadastrar(String nomeCompleto, String email, String senha) {

        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("E-mail já cadastrado");
        }

        if (!senha.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$")) {
            throw new IllegalArgumentException(
                    "A senha deve ter pelo menos 8 caracteres, uma letra maiúscula, uma letra minúscula, um número e um caractere especial"
            );
        }

        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(nomeCompleto);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setPerfil(Perfil.USUARIO);

        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}