package com.Vaniz.InfoShop.controller;

import com.Vaniz.InfoShop.dto.RegistroForm;
import com.Vaniz.InfoShop.service.CadastroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final CadastroService cadastroService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("registroForm", new RegistroForm());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute RegistroForm registroForm,
                            BindingResult result) {
        if (result.hasErrors()) {
            return "registro";
        }
        try {
            cadastroService.registrar(
                    registroForm.getNome(),
                    registroForm.getEmail(),
                    Long.valueOf(registroForm.getTelefone()),
                    registroForm.getSenha());
        } catch (IllegalArgumentException e) {
            result.rejectValue("email", "duplicado", e.getMessage());
            return "registro";
        }
        return "redirect:/login?registrado";
    }
}