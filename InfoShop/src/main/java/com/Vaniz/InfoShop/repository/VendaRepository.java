package com.Vaniz.InfoShop.repository;

import com.Vaniz.InfoShop.dto.VendasPorDia;
import com.Vaniz.InfoShop.model.Venda;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByClienteIdOrderByDataVendaDesc(Long clienteId);

    @Query("select sum(v.total) from Venda v where v.dataVenda >= :desde")
    BigDecimal somaTotal(@Param("desde") LocalDateTime desde);

    long countByDataVendaGreaterThanEqual(LocalDateTime desde);

    @Query(value = """
            SELECT DATE_FORMAT(data_venda, '%Y-%m-%d') AS dia,
                   SUM(total) AS total,
                   COUNT(*) AS qtd
            FROM venda
            WHERE data_venda >= :desde
            GROUP BY DATE_FORMAT(data_venda, '%Y-%m-%d')
            ORDER BY dia
            """, nativeQuery = true)
    List<VendasPorDia> vendasPorDia(@Param("desde") LocalDateTime desde);

    @Query("""
            select v from Venda v join fetch v.cliente
            where v.dataVenda >= :desde
            order by v.dataVenda desc
            """)
    List<Venda> ultimas(@Param("desde") LocalDateTime desde, Pageable pageable);

    @Query("""
                select distinct v from Venda v
                join fetch v.cliente
                left join fetch v.itens i
                left join fetch i.produto
                where v.id = :id
                """)
        Optional<Venda> buscarDetalhe(@Param("id") Long id);
}