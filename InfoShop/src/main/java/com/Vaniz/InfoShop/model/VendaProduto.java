package com.Vaniz.InfoShop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "venda_produto")
@Getter
@Setter
@NoArgsConstructor
public class VendaProduto {

    @EmbeddedId
    private VendaProdutoId id = new VendaProdutoId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("vendaId")
    @JoinColumn(name = "venda_id")
    private Venda venda;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("produtoId")
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade = 1;

    @Column(name = "valor_unitario", nullable = false, precision = 9, scale = 2)
    private BigDecimal valorUnitario;
}