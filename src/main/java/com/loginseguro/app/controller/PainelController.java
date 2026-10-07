package com.loginseguro.app.controller;

import com.loginseguro.app.model.Usuario;
import com.loginseguro.app.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PainelController {

    private final UsuarioService usuarioService;

    @GetMapping("/painel")
    public String painel(Authentication authentication) {
        boolean admin = possuiPapel(authentication, "ROLE_ADMIN");
        boolean moderador = possuiPapel(authentication, "ROLE_MODERADOR");

        if (admin) {
            return "redirect:/painel/admin";
        }
        if (moderador) {
            return "redirect:/painel/moderador";
        }
        return "redirect:/painel/usuario";
    }

    @GetMapping("/painel/usuario")
    public String painelUsuario(Authentication authentication, Model model) {
        carregarUsuarioLogado(authentication, model);
        return "painel/usuario";
    }

    @GetMapping("/painel/moderador")
    public String painelModerador(Authentication authentication, Model model) {
        carregarUsuarioLogado(authentication, model);
        return "painel/moderador";
    }

    @GetMapping("/painel/admin")
    public String painelAdmin(Authentication authentication, Model model) {
        carregarUsuarioLogado(authentication, model);
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "painel/admin";
    }

    private void carregarUsuarioLogado(Authentication authentication, Model model) {
        usuarioService.buscarPorEmail(authentication.getName())
                .ifPresent(usuario -> model.addAttribute("usuarioLogado", usuario));
    }

    private boolean possuiPapel(Authentication authentication, String papel) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals(papel)) {
                return true;
            }
        }
        return false;
    }

}
