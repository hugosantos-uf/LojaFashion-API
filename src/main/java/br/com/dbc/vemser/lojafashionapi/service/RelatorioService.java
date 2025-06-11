package br.com.dbc.vemser.lojafashionapi.service;

import br.com.dbc.vemser.lojafashionapi.dto.relatorios.RelatorioPedidoClienteDTO;
import br.com.dbc.vemser.lojafashionapi.dto.relatorios.RelatorioProdutoVendidoDTO;
import br.com.dbc.vemser.lojafashionapi.repository.ItemPedidoRepository;
import br.com.dbc.vemser.lojafashionapi.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public List<RelatorioProdutoVendidoDTO> gerarRelatorioProdutosVendidos() {
        return itemPedidoRepository.findProdutosMaisVendidos();
    }

    public List<RelatorioPedidoClienteDTO> gerarRelatorioPedidosPorCliente(Integer idCliente) {
        return pedidoRepository.findPedidosByCliente(idCliente);
    }
}