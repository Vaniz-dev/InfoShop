package com.Vaniz.InfoShop.dto;

import java.math.BigDecimal;

public interface ProdutoMaisVendido {
    Long getId();
    String getNome();
    Long getQuantidade();
    BigDecimal getReceita();
}