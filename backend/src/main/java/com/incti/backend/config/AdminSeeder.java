package com.incti.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.incti.backend.model.Perfil;
import com.incti.backend.model.Usuario;
import com.incti.backend.repository.UsuarioRepository;

/**
 * Cria o usuário administrador na primeira vez que o backend sobe,
 * a partir das variáveis de ambiente ADMIN_EMAIL e ADMIN_PASSWORD.
 * Se as variáveis não existirem (ex.: rodando local), não faz nada.
 */
@Component
public class AdminSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    private final UsuarioRepository usuarioRepository;
    private final String email;
    private final String senha;

    public AdminSeeder(
            UsuarioRepository usuarioRepository,
            @Value("${ADMIN_EMAIL:}") String email,
            @Value("${ADMIN_PASSWORD:}") String senha) {
        this.usuarioRepository = usuarioRepository;
        this.email = email.trim();
        this.senha = senha;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (email.isEmpty() || senha.isBlank()) {
            return;
        }
        if (usuarioRepository.existsByEmail(email)) {
            log.info("Administrador {} já existe.", email);
            return;
        }

        Usuario admin = new Usuario();
        admin.setNomeCompleto("Administrador");
        admin.setEmail(email);
        admin.setSenha(new BCryptPasswordEncoder().encode(senha));
        admin.setPerfil(Perfil.ADMIN);
        usuarioRepository.save(admin);
        log.info("Administrador {} criado.", email);
    }
}
