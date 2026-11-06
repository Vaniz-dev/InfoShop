package com.Vaniz.InfoShop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class BarraDia {
    private String rotulo;     // "06/11"
    private BigDecimal total;
    private long qtd;
    private int altura;        // 0 a 100 (%)
}