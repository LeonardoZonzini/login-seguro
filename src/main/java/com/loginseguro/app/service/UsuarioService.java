package com.loginseguro.app.service;

import com.loginseguro.app.dto.RegistroForm;
import com.loginseguro.app.exception.EmailJaCadastradoException;
import com.loginseguro.app.model.Papel;
import com.loginseguro.app.model.Usuario;
import com.loginseguro.app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario cadastrar(RegistroForm form) {
        String email = form.getEmail().trim().toLowerCase();

        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailJaCadastradoException(email);
        }

        Usuario usuario = Usuario.builder()
                .nome(form.getNome().trim())
                .email(email)
                .senha(passwordEncoder.encode(form.getSenha()))
                .papel(Papel.USUARIO)
                .ativo(true)
                .criadoEm(LocalDateTime.now())
                .build();

        return usuarioRepository.save(usuario);
    }

    public Usuario criarComPapel(String nome, String email, String senhaBruta, Papel papel) {
        Usuario usuario = Usuario.builder()
                .nome(nome)
                .email(email.trim().toLowerCase())
                .senha(passwordEncoder.encode(senhaBruta))
                .papel(papel)
                .ativo(true)
                .criadoEm(LocalDateTime.now())
                .build();

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email.trim().toLowerCase());
    }

    public Usuario alterarPapel(String id, Papel papel) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        usuario.setPapel(papel);
        return usuarioRepository.save(usuario);
    }

    public Usuario alternarAtivo(String id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        usuario.setAtivo(!usuario.isAtivo());
        return usuarioRepository.save(usuario);
    }

}
