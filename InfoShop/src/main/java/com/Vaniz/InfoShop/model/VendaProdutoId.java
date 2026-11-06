package com.Vaniz.InfoShop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class VendaProdutoId implements Serializable {

    @Column(name = "venda_id")
    private Long vendaId;

    @Column(name = "produto_id")
    private Long produtoId;
}