package com.loginseguro.app.controller;

import com.loginseguro.app.dto.RegistroForm;
import com.loginseguro.app.exception.EmailJaCadastradoException;
import com.loginseguro.app.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AutenticacaoController {

    private final UsuarioService usuarioService;

    @GetMapping("/")
    public String raiz(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/painel";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/registro")
    public String formularioRegistro(Model model) {
        model.addAttribute("registroForm", new RegistroForm());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String processarRegistro(@Valid @ModelAttribute RegistroForm registroForm,
                                     BindingResult bindingResult,
                                     Model model) {
        if (!registroForm.getSenha().equals(registroForm.getConfirmarSenha())) {
            bindingResult.rejectValue("confirmarSenha", "senha.diferente", "As senhas não coincidem");
        }

        if (bindingResult.hasErrors()) {
            return "auth/registro";
        }

        try {
            usuarioService.cadastrar(registroForm);
        } catch (EmailJaCadastradoException e) {
            bindingResult.rejectValue("email", "email.duplicado", e.getMessage());
            return "auth/registro";
        }

        return "redirect:/login?cadastrado";
    }

    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "erro/acesso-negado";
    }

}
