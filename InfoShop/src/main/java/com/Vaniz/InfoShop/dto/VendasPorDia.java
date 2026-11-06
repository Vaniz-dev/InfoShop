package com.Vaniz.InfoShop.dto;

import java.math.BigDecimal;

public interface VendasPorDia {
    String getDia();       // "2026-11-06"
    BigDecimal getTotal();
    Long getQtd();
}