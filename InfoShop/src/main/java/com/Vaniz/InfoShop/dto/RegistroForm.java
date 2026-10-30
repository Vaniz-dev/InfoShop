package com.Vaniz.InfoShop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroForm {

    @NotBlank(message = "Informe o nome")
    @Size(max = 75, message = "O nome pode ter no máximo 75 caracteres")
    private String nome;

    @NotBlank(message = "Informe o email")
    @Email(message = "Email inválido")
    @Size(max = 150, message = "O email pode ter no máximo 150 caracteres")
    private String email;

    @NotBlank(message = "Informe o telefone")
    @Pattern(regexp = "\\d{10,13}", message = "Digite só números, com DDD")
    private String telefone;

    @NotBlank(message = "Informe a senha")
    @Size(min = 6, max = 60, message = "A senha deve ter entre 6 e 60 caracteres")
    private String senha;
}