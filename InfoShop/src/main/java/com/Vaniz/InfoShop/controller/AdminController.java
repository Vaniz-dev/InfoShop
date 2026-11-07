package com.Vaniz.InfoShop.controller;

import com.Vaniz.InfoShop.model.Usuario;
import com.Vaniz.InfoShop.model.Venda;
import com.Vaniz.InfoShop.repository.UsuarioRepository;
import com.Vaniz.InfoShop.repository.VendaRepository;
import com.Vaniz.InfoShop.service.Carrinho;
import com.Vaniz.InfoShop.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final DashboardService dashboardService;
    private final UsuarioRepository usuarioRepo;
    private final VendaRepository vendaRepo;
    private final Carrinho carrinho;

    @GetMapping("/painel")
    public String painel(@RequestParam(defaultValue = "todas") String periodo,
                         Authentication auth, Model model) {
        preencherTopo(auth, model);
        model.addAttribute("d", dashboardService.montar(periodo));
        return "admin/painel";
    }

    @GetMapping("/vendas/{id}")
    public String venda(@PathVariable Long id, Authentication auth, Model model) {
        Venda venda = vendaRepo.buscarDetalhe(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda não encontrada"));

        preencherTopo(auth, model);
        model.addAttribute("venda", venda);
        return "admin/venda";
    }

    private void preencherTopo(Authentication auth, Model model) {
        Usuario usuario = usuarioRepo.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("nome", usuario.getNome());
        model.addAttribute("ehAdmin", true);
        model.addAttribute("qtdCarrinho", carrinho.getQuantidadeTotal());
    }
}