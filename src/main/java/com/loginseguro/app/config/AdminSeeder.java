package com.loginseguro.app.config;

import com.loginseguro.app.model.Papel;
import com.loginseguro.app.repository.UsuarioRepository;
import com.loginseguro.app.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    @Value("${app.admin.email:}")
    private String adminEmail;

    @Value("${app.admin.senha:}")
    private String adminSenha;

    @Override
    public void run(String... args) {
        if (!StringUtils.hasText(adminEmail) || !StringUtils.hasText(adminSenha)) {
            return;
        }

        if (usuarioRepository.existsByEmail(adminEmail.trim().toLowerCase())) {
            return;
        }

        usuarioService.criarComPapel("Administrador", adminEmail, adminSenha, Papel.ADMIN);
    }

}
