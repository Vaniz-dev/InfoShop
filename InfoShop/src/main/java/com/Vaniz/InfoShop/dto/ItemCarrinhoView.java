package com.Vaniz.InfoShop.dto;

import com.Vaniz.InfoShop.model.Produto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ItemCarrinhoView {
    private Produto produto;
    private int quantidade;
    private BigDecimal subtotal;
}