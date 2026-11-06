package com.Vaniz.InfoShop.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 75)
    private String nome;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 100)
    private String senha;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal saldo = new BigDecimal("8000.00");

    private Long telefone;

    @Convert(converter = PerfilConverter.class)
    @Column(name = "nivel", nullable = false)
    private Perfil nivel = Perfil.USER;
}