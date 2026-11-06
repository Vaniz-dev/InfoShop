package com.Vaniz.InfoShop.service;

import com.Vaniz.InfoShop.model.Produto;
import com.Vaniz.InfoShop.model.Usuario;
import com.Vaniz.InfoShop.model.Venda;
import com.Vaniz.InfoShop.model.VendaProduto;
import com.Vaniz.InfoShop.repository.ProdutoRepository;
import com.Vaniz.InfoShop.repository.UsuarioRepository;
import com.Vaniz.InfoShop.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final UsuarioRepository usuarioRepo;
    private final ProdutoRepository produtoRepo;
    private final VendaRepository vendaRepo;

    /** Tudo acontece numa transação: se algo falhar, nada é gravado e o saldo não muda. */
    @Transactional
    public Venda finalizar(String email, Map<Long, Integer> itensCarrinho) {
        if (itensCarrinho.isEmpty()) {
            throw new IllegalStateException("O carrinho está vazio");
        }

        Usuario cliente = usuarioRepo.buscarParaAtualizar(email)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado"));

        Map<Long, Produto> produtos = produtoRepo.findAllById(itensCarrinho.keySet()).stream()
                .collect(Collectors.toMap(Produto::getId, Function.identity()));

        Venda venda = new Venda();
        venda.setCliente(cliente);
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entrada : itensCarrinho.entrySet()) {
            Produto produto = produtos.get(entrada.getKey());
            if (produto == null) {
                throw new IllegalStateException("Um produto do carrinho não existe mais");
            }
            int quantidade = entrada.getValue();

            VendaProduto item = new VendaProduto();
            item.setVenda(venda);
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setValorUnitario(produto.getValor()); // preço congelado no momento da compra
            venda.getItens().add(item);

            total = total.add(produto.getValor().multiply(BigDecimal.valueOf(quantidade)));
        }

        if (cliente.getSaldo().compareTo(total) < 0) {
            throw new IllegalStateException("Saldo insuficiente para esta compra");
        }

        cliente.setSaldo(cliente.getSaldo().subtract(total));
        venda.setTotal(total);
        return vendaRepo.save(venda);
    }
}