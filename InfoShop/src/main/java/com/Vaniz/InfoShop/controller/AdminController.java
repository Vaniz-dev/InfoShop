package com.Vaniz.InfoShop.controller;

import com.Vaniz.InfoShop.model.Usuario;
import com.Vaniz.InfoShop.repository.UsuarioRepository;
import com.Vaniz.InfoShop.service.Carrinho;
import com.Vaniz.InfoShop.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final DashboardService dashboardService;
    private final UsuarioRepository usuarioRepo;
    private final Carrinho carrinho;

    @GetMapping("/painel")
    public String painel(@RequestParam(defaultValue = "todas") String periodo,
                        Authentication auth, Model model) {
        Usuario usuario = usuarioRepo.findByEmail(auth.getName()).orElseThrow();

        model.addAttribute("nome", usuario.getNome());
        model.addAttribute("ehAdmin", true);
        model.addAttribute("qtdCarrinho", carrinho.getQuantidadeTotal());
        model.addAttribute("d", dashboardService.montar(periodo));
        return "admin/painel";
    }
}