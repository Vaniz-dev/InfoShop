package com.Vaniz.InfoShop.repository;

import com.Vaniz.InfoShop.dto.ProdutoMaisVendido;
import com.Vaniz.InfoShop.model.VendaProduto;
import com.Vaniz.InfoShop.model.VendaProdutoId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VendaProdutoRepository extends JpaRepository<VendaProduto, VendaProdutoId> {

    @Query("""
            select p.id as id, p.nome as nome,
                   sum(vp.quantidade) as quantidade,
                   sum(vp.quantidade * vp.valorUnitario) as receita
            from VendaProduto vp
                 join vp.venda v
                 join vp.produto p
            where v.dataVenda >= :desde
            group by p.id, p.nome
            order by sum(vp.quantidade) desc
            """)
    List<ProdutoMaisVendido> maisVendidos(@Param("desde") LocalDateTime desde, Pageable pageable);
}