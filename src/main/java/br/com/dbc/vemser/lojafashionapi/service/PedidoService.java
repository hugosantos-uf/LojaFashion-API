package br.com.dbc.vemser.lojafashionapi.service;

import br.com.dbc.vemser.lojafashionapi.dto.cliente.ClienteDTO;
import br.com.dbc.vemser.lojafashionapi.dto.pedido.ItemPedidoDTO;
import br.com.dbc.vemser.lojafashionapi.dto.pedido.PedidoCreateDTO;
import br.com.dbc.vemser.lojafashionapi.dto.pedido.PedidoDTO;
import br.com.dbc.vemser.lojafashionapi.entity.ClienteEntity;
import br.com.dbc.vemser.lojafashionapi.entity.ItemPedidoEntity;
import br.com.dbc.vemser.lojafashionapi.entity.PedidoEntity;
import br.com.dbc.vemser.lojafashionapi.entity.ProdutoEntity;
import br.com.dbc.vemser.lojafashionapi.enums.StatusPedido;
import br.com.dbc.vemser.lojafashionapi.exception.RegraDeNegocioException;
import br.com.dbc.vemser.lojafashionapi.repository.ItemPedidoRepository;
import br.com.dbc.vemser.lojafashionapi.repository.PedidoRepository;
import br.com.dbc.vemser.lojafashionapi.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;
    private final ProdutoRepository produtoRepository;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    @Transactional(rollbackFor = Exception.class)
    public PedidoDTO create(PedidoCreateDTO pedidoCreateDTO) throws RegraDeNegocioException {
        log.info("Criando pedido...");
        ClienteEntity clienteEntity = clienteService.findClienteById(pedidoCreateDTO.getIdCliente());

        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setCliente(clienteEntity);
        pedidoEntity.setDataPedido(LocalDate.now());
        pedidoEntity.setStatusPedido(StatusPedido.PENDENTE);

        Set<ItemPedidoEntity> itens = new HashSet<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (var itemCreateDTO : pedidoCreateDTO.getItens()) {
            ProdutoEntity produtoEntity = produtoService.findProdutoById(itemCreateDTO.getIdProduto());

            if (produtoEntity.getQuantidadeEstoque() < itemCreateDTO.getQuantidade()) {
                throw new RegraDeNegocioException("Estoque insuficiente para o produto: " + produtoEntity.getNome());
            }

            ItemPedidoEntity itemPedidoEntity = new ItemPedidoEntity();
            itemPedidoEntity.setProduto(produtoEntity);
            itemPedidoEntity.setQuantidade(itemCreateDTO.getQuantidade());
            itemPedidoEntity.setPrecoUnitario(produtoEntity.getPreco());
            itemPedidoEntity.setPedido(pedidoEntity);
            itens.add(itemPedidoEntity);

            valorTotal = valorTotal.add(produtoEntity.getPreco().multiply(BigDecimal.valueOf(itemCreateDTO.getQuantidade())));

            int novoEstoque = produtoEntity.getQuantidadeEstoque() - itemCreateDTO.getQuantidade();
            produtoEntity.setQuantidadeEstoque(novoEstoque);
            produtoRepository.save(produtoEntity);
            log.info("Estoque do produto ID {} atualizado para {}.", produtoEntity.getIdProduto(), novoEstoque);
        }

        pedidoEntity.setItens(itens);
        pedidoEntity.setValorTotalPedido(valorTotal);

        PedidoEntity pedidoCriado = pedidoRepository.save(pedidoEntity);
        log.info("Pedido ID {} criado com sucesso.", pedidoCriado.getIdPedido());

        PedidoDTO pedidoDTO = pedidoToPedidoDTO(pedidoCriado);

        Map<String, Object> dadosEmail = Map.of(
                "nomeCliente", pedidoDTO.getCliente().getNomeCompleto(),
                "idPedido", pedidoDTO.getIdPedido().toString(),
                "statusPedido", pedidoDTO.getStatusPedido().name(),
                "valorTotal", pedidoDTO.getValorTotalPedido(),
                "itens", pedidoDTO.getItens() // Passamos a lista inteira de itens
        );

        emailService.sendEmail("Confirmação do seu Pedido #" + pedidoDTO.getIdPedido(),
                "pedido-confirmacao-template.ftl",
                dadosEmail,
                clienteEntity.getEmail());
        return pedidoDTO;
    }

    public List<PedidoDTO> listAll() {
        return pedidoRepository.findAll().stream()
                .map(this::pedidoToPedidoDTO)
                .collect(Collectors.toList());
    }

    public PedidoDTO getById(Integer idPedido) throws RegraDeNegocioException {
        PedidoEntity pedidoEntity = findPedidoById(idPedido);
        return pedidoToPedidoDTO(pedidoEntity);
    }

    public List<PedidoDTO> getByClienteId(Integer idCliente) throws RegraDeNegocioException {
        clienteService.findClienteById(idCliente);
        return pedidoRepository.findByCliente_IdCliente(idCliente).stream()
                .map(this::pedidoToPedidoDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PedidoDTO updateStatus(Integer idPedido, StatusPedido novoStatus) throws RegraDeNegocioException {
        log.info("Atualizando status do pedido ID {} para {}", idPedido, novoStatus);
        PedidoEntity pedidoRecuperado = findPedidoById(idPedido);

        if (pedidoRecuperado.getStatusPedido() == StatusPedido.CANCELADO || pedidoRecuperado.getStatusPedido() == StatusPedido.ENTREGUE) {
            throw new RegraDeNegocioException("Não é possível alterar o status de um pedido que já foi entregue ou cancelado.");
        }

        if (novoStatus == StatusPedido.CANCELADO && pedidoRecuperado.getStatusPedido() == StatusPedido.ENVIADO) {
            throw new RegraDeNegocioException("Não é possível cancelar um pedido que já foi enviado.");
        }

        pedidoRecuperado.setStatusPedido(novoStatus);
        PedidoEntity pedidoAtualizado = pedidoRepository.save(pedidoRecuperado);

        log.info("Status do pedido ID {} atualizado.", idPedido);
        return pedidoToPedidoDTO(pedidoAtualizado);
    }

    @Transactional
    public void delete(Integer idPedido) throws RegraDeNegocioException {
        log.warn("Deletando pedido ID: {}", idPedido);
        PedidoEntity pedidoEntity = findPedidoById(idPedido);

        if (pedidoEntity.getStatusPedido() != StatusPedido.PENDENTE && pedidoEntity.getStatusPedido() != StatusPedido.CANCELADO) {
            throw new RegraDeNegocioException("Só é possível deletar pedidos com status PENDENTE ou CANCELADO.");
        }

        // Ao deletar o pedido, o estoque não será devolvido neste exemplo.
        // A anotação cascade=CascadeType.ALL e orphanRemoval=true na entidade Pedido
        // irá garantir que os itens do pedido sejam removidos junto com o pedido.

        pedidoRepository.delete(pedidoEntity);
        log.info("Pedido ID {} deletado.", idPedido);
    }

    public PedidoEntity findPedidoById(Integer idPedido) throws RegraDeNegocioException {
        return pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RegraDeNegocioException("Pedido não encontrado com o ID: " + idPedido));
    }

    private PedidoDTO pedidoToPedidoDTO(PedidoEntity pedidoEntity) {
        PedidoDTO pedidoDTO = objectMapper.convertValue(pedidoEntity, PedidoDTO.class);
        pedidoDTO.setCliente(objectMapper.convertValue(pedidoEntity.getCliente(), ClienteDTO.class));

        List<ItemPedidoDTO> itensDTO = pedidoEntity.getItens().stream()
                .map(item -> {
                    ItemPedidoDTO itemDTO = new ItemPedidoDTO();
                    itemDTO.setProduto(produtoService.produtoToProdutoDTO(item.getProduto()));
                    itemDTO.setQuantidade(item.getQuantidade());
                    itemDTO.setPrecoUnitario(item.getPrecoUnitario());
                    return itemDTO;
                }).collect(Collectors.toList());

        pedidoDTO.setItens(itensDTO);
        return pedidoDTO;
    }
}