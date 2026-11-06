package com.Vaniz.InfoShop.controller;

import com.Vaniz.InfoShop.model.Produto;
import com.Vaniz.InfoShop.model.Usuario;
import com.Vaniz.InfoShop.repository.ProdutoRepository;
import com.Vaniz.InfoShop.repository.UsuarioRepository;
import com.Vaniz.InfoShop.service.Carrinho;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoRepository produtoRepo;
    private final UsuarioRepository usuarioRepo;
    private final Carrinho carrinho;

    @GetMapping("/produto/{id}")
    public String ver(@PathVariable Long id, Authentication auth, Model model) {
        Produto produto = produtoRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

        Usuario usuario = usuarioRepo.findByEmail(auth.getName()).orElseThrow();
        boolean ehAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("produto", produto);
        model.addAttribute("nome", usuario.getNome());
        model.addAttribute("ehAdmin", ehAdmin);
        model.addAttribute("qtdCarrinho", carrinho.getQuantidadeTotal());
        return "produto";
    }
}