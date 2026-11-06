package com.Vaniz.InfoShop.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@SessionScope
public class Carrinho {

    private final Map<Long, Integer> itens = new LinkedHashMap<>(); // produtoId -> quantidade

    public void adicionar(Long produtoId) {
        itens.merge(produtoId, 1, (atual, novo) -> Math.min(atual + novo, 99));
    }

    public void remover(Long produtoId) {
        itens.remove(produtoId);
    }

    public void limpar() {
        itens.clear();
    }

    public void diminuir(Long produtoId) {
        itens.computeIfPresent(produtoId, (id, qtd) -> qtd > 1 ? qtd - 1 : null);
    }

    public Map<Long, Integer> getItens() {
        return Collections.unmodifiableMap(itens);
    }

    public int getQuantidadeTotal() {
        return itens.values().stream().mapToInt(Integer::intValue).sum();
    }
}