package br.com.dbc.vemser.lojafashionapi.controller;

import br.com.dbc.vemser.lojafashionapi.dto.relatorios.RelatorioPedidoClienteDTO;
import br.com.dbc.vemser.lojafashionapi.dto.relatorios.RelatorioProdutoVendidoDTO;
import br.com.dbc.vemser.lojafashionapi.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/relatorio")
@RequiredArgsConstructor
@Tag(name = "Relatório", description = "Endpoints para geração de relatórios")
public class RelatorioController {
    private final RelatorioService relatorioService;

    @Operation(summary = "Gerar relatório de pedidos por cliente", description = "Gera um relatório listando os pedidos de um cliente específico. Se nenhum ID for fornecido, lista de todos os clientes.")
    @GetMapping("/pedidos-por-cliente")
    public ResponseEntity<List<RelatorioPedidoClienteDTO>> gerarRelatorioPedidosPorCliente(@RequestParam(required = false) Integer idCliente) {
        return ResponseEntity.ok(relatorioService.gerarRelatorioPedidosPorCliente(idCliente));
    }

    @Operation(summary = "Gerar relatório de produtos mais vendidos", description = "Gera um relatório dos produtos mais vendidos, ordenado por quantidade.")
    @GetMapping("/produtos-vendidos")
    public ResponseEntity<List<RelatorioProdutoVendidoDTO>> gerarRelatorioProdutosVendidos() {
        return ResponseEntity.ok(relatorioService.gerarRelatorioProdutosVendidos());
    }
}