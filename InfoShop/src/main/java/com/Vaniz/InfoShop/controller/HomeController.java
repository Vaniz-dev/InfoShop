package com.Vaniz.InfoShop.controller;

import com.Vaniz.InfoShop.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UsuarioRepository repo;

    @GetMapping("/")
    public String raiz() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Authentication auth, Model model) {
        boolean ehAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        String nome = repo.findByEmail(auth.getName())
                .map(u -> u.getNome())
                .orElse(auth.getName());

        model.addAttribute("nome", nome);
        model.addAttribute("ehAdmin", ehAdmin);
        return "home";
    }
}