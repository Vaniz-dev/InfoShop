package com.Vaniz.InfoShop.dto;

import com.Vaniz.InfoShop.model.Venda;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardView {
    private String periodo;         // "todas", "30", "7" ou "3"
    private String rotuloPeriodo;   // "Últimos 7 dias"
    private BigDecimal faturamento;
    private long totalVendas;
    private BigDecimal ticketMedio;
    private long totalClientes;
    private long totalProdutos;
    private List<BarraDia> dias;
    private List<ProdutoMaisVendido> maisVendidos;
    private long maxQtd;
    private List<Venda> ultimas;
}