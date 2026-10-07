package com.loginseguro.app.service;

import com.loginseguro.app.dto.RegistroForm;
import com.loginseguro.app.exception.EmailJaCadastradoException;
import com.loginseguro.app.model.Papel;
import com.loginseguro.app.model.Usuario;
import com.loginseguro.app.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void cadastraComSenhaCriptografada() {
        RegistroForm form = new RegistroForm();
        form.setNome("Maria Silva");
        form.setEmail("Maria@Exemplo.com");
        form.setSenha("senha12345");
        form.setConfirmarSenha("senha12345");

        when(usuarioRepository.existsByEmail("maria@exemplo.com")).thenReturn(false);
        when(passwordEncoder.encode("senha12345")).thenReturn("hash-simulado");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Usuario usuario = usuarioService.cadastrar(form);

        assertThat(usuario.getSenha()).isEqualTo("hash-simulado");
        assertThat(usuario.getEmail()).isEqualTo("maria@exemplo.com");
        assertThat(usuario.getPapel()).isEqualTo(Papel.USUARIO);
        assertThat(usuario.isAtivo()).isTrue();
    }

    @Test
    void naoPermiteEmailDuplicado() {
        RegistroForm form = new RegistroForm();
        form.setNome("Maria Silva");
        form.setEmail("maria@exemplo.com");
        form.setSenha("senha12345");
        form.setConfirmarSenha("senha12345");

        when(usuarioRepository.existsByEmail("maria@exemplo.com")).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.cadastrar(form))
                .isInstanceOf(EmailJaCadastradoException.class);
    }

}
