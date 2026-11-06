package com.Vaniz.InfoShop.service;

import com.Vaniz.InfoShop.dto.BarraDia;
import com.Vaniz.InfoShop.dto.DashboardView;
import com.Vaniz.InfoShop.dto.ProdutoMaisVendido;
import com.Vaniz.InfoShop.dto.VendasPorDia;
import com.Vaniz.InfoShop.model.Perfil;
import com.Vaniz.InfoShop.model.Venda;
import com.Vaniz.InfoShop.repository.ProdutoRepository;
import com.Vaniz.InfoShop.repository.UsuarioRepository;
import com.Vaniz.InfoShop.repository.VendaProdutoRepository;
import com.Vaniz.InfoShop.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    /** Data antiga o bastante para representar "todas as vendas". */
    private static final LocalDateTime INICIO_DOS_TEMPOS = LocalDateTime.of(2000, 1, 1, 0, 0);

    /** Com "todas as vendas", o gráfico mostra os últimos 30 dias. */
    private static final int DIAS_GRAFICO_TODAS = 30;

    private final VendaRepository vendaRepo;
    private final VendaProdutoRepository vendaProdutoRepo;
    private final UsuarioRepository usuarioRepo;
    private final ProdutoRepository produtoRepo;

    @Transactional(readOnly = true)
    public DashboardView montar(String periodo) {
        // Só aceita valores conhecidos; qualquer outro vira "todas"
        int dias = switch (periodo == null ? "" : periodo) {
            case "3" -> 3;
            case "7" -> 7;
            case "30" -> 30;
            default -> 0; // 0 = todas as vendas
        };

        String chave = dias == 0 ? "todas" : String.valueOf(dias);
        String rotulo = dias == 0 ? "Todas as vendas" : "Últimos " + dias + " dias";

        // "Últimos 3 dias" = hoje e os 2 dias anteriores
        LocalDateTime desde = dias == 0
                ? INICIO_DOS_TEMPOS
                : LocalDate.now().minusDays(dias - 1).atStartOfDay();

        BigDecimal faturamento = vendaRepo.somaTotal(desde);
        if (faturamento == null) faturamento = BigDecimal.ZERO;

        long totalVendas = vendaRepo.countByDataVendaGreaterThanEqual(desde);
        BigDecimal ticketMedio = totalVendas == 0
                ? BigDecimal.ZERO
                : faturamento.divide(BigDecimal.valueOf(totalVendas), 2, RoundingMode.HALF_UP);

        List<ProdutoMaisVendido> maisVendidos = vendaProdutoRepo.maisVendidos(desde, PageRequest.of(0, 5));
        long maxQtd = maisVendidos.stream().mapToLong(ProdutoMaisVendido::getQuantidade).max().orElse(1);

        List<Venda> ultimas = vendaRepo.ultimas(desde, PageRequest.of(0, 8));

        int diasGrafico = dias == 0 ? DIAS_GRAFICO_TODAS : dias;

        return new DashboardView(
                chave,
                rotulo,
                faturamento,
                totalVendas,
                ticketMedio,
                usuarioRepo.countByNivel(Perfil.USER),
                produtoRepo.count(),
                montarGrafico(diasGrafico),
                maisVendidos,
                maxQtd,
                ultimas
        );
    }

    /** Sempre devolve uma coluna por dia, inclusive dias sem venda (altura 0). */
    private List<BarraDia> montarGrafico(int quantidadeDeDias) {
        LocalDate inicio = LocalDate.now().minusDays(quantidadeDeDias - 1);

        Map<LocalDate, VendasPorDia> porDia = vendaRepo.vendasPorDia(inicio.atStartOfDay()).stream()
                .collect(Collectors.toMap(v -> LocalDate.parse(v.getDia()), Function.identity()));

        BigDecimal maior = porDia.values().stream()
                .map(VendasPorDia::getTotal)
                .max(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM");
        List<BarraDia> barras = new ArrayList<>();

        for (int i = 0; i < quantidadeDeDias; i++) {
            LocalDate dia = inicio.plusDays(i);
            VendasPorDia v = porDia.get(dia);

            BigDecimal total = v == null ? BigDecimal.ZERO : v.getTotal();
            long qtd = v == null ? 0 : v.getQtd();

            int altura = 0;
            if (total.signum() > 0 && maior.signum() > 0) {
                altura = Math.max(3, total.multiply(BigDecimal.valueOf(100))
                        .divide(maior, 0, RoundingMode.HALF_UP).intValue());
            }
            barras.add(new BarraDia(dia.format(formato), total, qtd, altura));
        }
        return barras;
    }
}