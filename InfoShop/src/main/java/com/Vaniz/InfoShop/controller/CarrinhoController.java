package com.Vaniz.InfoShop.controller;

import com.Vaniz.InfoShop.dto.ItemCarrinhoView;
import com.Vaniz.InfoShop.model.Produto;
import com.Vaniz.InfoShop.model.Usuario;
import com.Vaniz.InfoShop.model.Venda;
import com.Vaniz.InfoShop.repository.ProdutoRepository;
import com.Vaniz.InfoShop.repository.UsuarioRepository;
import com.Vaniz.InfoShop.service.Carrinho;
import com.Vaniz.InfoShop.service.VendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/carrinho")
@RequiredArgsConstructor
public class CarrinhoController {

    private final Carrinho carrinho;
    private final ProdutoRepository produtoRepo;
    private final UsuarioRepository usuarioRepo;
    private final VendaService vendaService;

    @GetMapping
    public String ver(Authentication auth, Model model) {
        Map<Long, Produto> produtos = produtoRepo.findAllById(carrinho.getItens().keySet()).stream()
                .collect(Collectors.toMap(Produto::getId, Function.identity()));

        List<ItemCarrinhoView> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        int quantidade = 0;

        for (Map.Entry<Long, Integer> e : carrinho.getItens().entrySet()) {
            Produto p = produtos.get(e.getKey());
            if (p == null) continue;
            BigDecimal subtotal = p.getValor().multiply(BigDecimal.valueOf(e.getValue()));
            itens.add(new ItemCarrinhoView(p, e.getValue(), subtotal));
            total = total.add(subtotal);
            quantidade += e.getValue();
        }

        Usuario usuario = usuarioRepo.findByEmail(auth.getName()).orElseThrow();
        boolean ehAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("itens", itens);
        model.addAttribute("total", total);
        model.addAttribute("saldo", usuario.getSaldo());
        model.addAttribute("saldoApos", usuario.getSaldo().subtract(total));
        model.addAttribute("saldoSuficiente", usuario.getSaldo().compareTo(total) >= 0);
        model.addAttribute("nome", usuario.getNome());
        model.addAttribute("ehAdmin", ehAdmin);
        model.addAttribute("qtdCarrinho", quantidade);
        return "carrinho";
    }

    @PostMapping("/adicionar/{id}")
    public String adicionar(@PathVariable Long id,
                            @RequestParam(defaultValue = "false") boolean voltar,
                            RedirectAttributes ra) {
        if (!produtoRepo.existsById(id)) {
            ra.addFlashAttribute("erro", "Produto não encontrado");
            return "redirect:/home";
        }
        carrinho.adicionar(id);
        ra.addFlashAttribute("sucesso", "Produto adicionado ao carrinho");
        return voltar ? "redirect:/produto/" + id : "redirect:/home";
    }

    @PostMapping("/mais/{id}")
    public String mais(@PathVariable Long id) {
        if (produtoRepo.existsById(id)) {
            carrinho.adicionar(id);
        }
        return "redirect:/carrinho";
    }

    @PostMapping("/menos/{id}")
    public String menos(@PathVariable Long id) {
        carrinho.diminuir(id);
        return "redirect:/carrinho";
    }

    @PostMapping("/remover/{id}")
    public String remover(@PathVariable Long id) {
        carrinho.remover(id);
        return "redirect:/carrinho";
    }

    @PostMapping("/limpar")
    public String limpar() {
        carrinho.limpar();
        return "redirect:/carrinho";
    }

    @PostMapping("/finalizar")
    public String finalizar(Authentication auth, RedirectAttributes ra) {
        try {
            Venda venda = vendaService.finalizar(auth.getName(), carrinho.getItens());
            carrinho.limpar();
            ra.addFlashAttribute("sucesso", "Compra #" + venda.getId() + " realizada com sucesso!");
            return "redirect:/home";
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/carrinho";
        }
    }
}