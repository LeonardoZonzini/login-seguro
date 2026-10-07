package com.loginseguro.app.controller;

import com.loginseguro.app.model.Papel;
import com.loginseguro.app.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsuariosController {

    private final UsuarioService usuarioService;

    @PostMapping("/painel/admin/usuarios/{id}/papel")
    public String alterarPapel(@PathVariable String id, @RequestParam Papel papel) {
        usuarioService.alterarPapel(id, papel);
        return "redirect:/painel/admin";
    }

    @PostMapping("/painel/admin/usuarios/{id}/status")
    public String alternarStatus(@PathVariable String id) {
        usuarioService.alternarAtivo(id);
        return "redirect:/painel/admin";
    }

}
