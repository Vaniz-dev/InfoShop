package com.Vaniz.InfoShop.repository;

import com.Vaniz.InfoShop.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}