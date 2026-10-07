package com.loginseguro.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistroForm {

    @NotBlank(message = "Informe seu nome")
    @Size(min = 2, max = 120, message = "Nome deve ter entre 2 e 120 caracteres")
    private String nome;

    @NotBlank(message = "Informe seu e-mail")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "Informe uma senha")
    @Size(min = 8, max = 72, message = "Senha deve ter no mínimo 8 caracteres")
    private String senha;

    @NotBlank(message = "Confirme sua senha")
    private String confirmarSenha;

}
