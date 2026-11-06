package com.Vaniz.InfoShop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 75)
    private String nome;

    @Column(nullable = false, precision = 9, scale = 2)
    private BigDecimal valor;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "caminho_imagem", columnDefinition = "TEXT")
    private String caminhoImagem;
}