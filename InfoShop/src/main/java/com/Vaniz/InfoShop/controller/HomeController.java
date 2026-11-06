package com.Vaniz.InfoShop.controller;

import com.Vaniz.InfoShop.model.Usuario;
import com.Vaniz.InfoShop.repository.ProdutoRepository;
import com.Vaniz.InfoShop.repository.UsuarioRepository;
import com.Vaniz.InfoShop.service.Carrinho;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UsuarioRepository usuarioRepo;
    private final ProdutoRepository produtoRepo;
    private final Carrinho carrinho;

    @GetMapping("/")
    public String raiz() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Authentication auth, Model model) {
        Usuario usuario = usuarioRepo.findByEmail(auth.getName()).orElseThrow();

        boolean ehAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("nome", usuario.getNome());
        model.addAttribute("saldo", usuario.getSaldo());
        model.addAttribute("ehAdmin", ehAdmin);
        model.addAttribute("produtos", produtoRepo.findAll());
        model.addAttribute("qtdCarrinho", carrinho.getQuantidadeTotal());
        return "home";
    }
}