package br.com.dbc.vemser.lojafashionapi.controller;

import br.com.dbc.vemser.lojafashionapi.dto.pedido.PedidoCreateDTO;
import br.com.dbc.vemser.lojafashionapi.dto.pedido.PedidoDTO;
import br.com.dbc.vemser.lojafashionapi.enums.StatusPedido;
import br.com.dbc.vemser.lojafashionapi.exception.BancoDeDadosException;
import br.com.dbc.vemser.lojafashionapi.exception.RegraDeNegocioException;
import br.com.dbc.vemser.lojafashionapi.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pedido")
@Validated
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Pedido", description = "Endpoints para gerenciamento de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @Operation(summary = "Criar um novo pedido", description = "Cria um novo pedido para um cliente com um ou mais produtos e envia e-mail de confirmação.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso.")
            }
    )
    @PostMapping
    public ResponseEntity<PedidoDTO> create(@Valid @RequestBody PedidoCreateDTO pedidoCreateDTO) throws RegraDeNegocioException, BancoDeDadosException {
        log.info("Recebida requisição para criar pedido para o Cliente ID {} com {} item(ns).",
                pedidoCreateDTO.getIdCliente(), pedidoCreateDTO.getItens().size());

        PedidoDTO pedidoCriado = pedidoService.create(pedidoCreateDTO);
        log.info("Pedido ID {} criado com sucesso!", pedidoCriado.getIdPedido());
        return new ResponseEntity<>(pedidoCriado, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todos os pedidos", description = "Retorna uma lista com todos os pedidos cadastrados.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso.")})
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listAll() throws BancoDeDadosException, RegraDeNegocioException {
        log.info("Recebida requisição para listar todos os pedidos.");
        List<PedidoDTO> pedidos = pedidoService.listAll();
        return ResponseEntity.ok(pedidos);
    }

    @Operation(summary = "Buscar pedido por ID", description = "Retorna os detalhes de um pedido específico a partir do seu ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Pedido retornado com sucesso.")
            }
    )
    @GetMapping("/{idPedido}")
    public ResponseEntity<PedidoDTO> getById(@PathVariable("idPedido") Integer idPedido) throws RegraDeNegocioException, BancoDeDadosException {
        log.info("Recebida requisição para buscar pedido com ID: {}", idPedido);
        PedidoDTO pedido = pedidoService.getById(idPedido);
        return ResponseEntity.ok(pedido);
    }

    @Operation(summary = "Listar pedidos por cliente", description = "Retorna uma lista de pedidos feitos por um cliente específico.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Lista de pedidos do cliente retornada com sucesso.")
            }
    )
    @GetMapping("/por-cliente/{idCliente}")
    public ResponseEntity<List<PedidoDTO>> getByClienteId(@PathVariable("idCliente") Integer idCliente) throws RegraDeNegocioException, BancoDeDadosException {
        log.info("Buscando pedidos do cliente ID: {}", idCliente);
        List<PedidoDTO> pedidos = pedidoService.getByClienteId(idCliente);
        return ResponseEntity.ok(pedidos);
    }


    @Operation(summary = "Atualizar status de um pedido", description = "Atualiza o status de um pedido existente (ex: PENDENTE para PAGO).")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Status do pedido atualizado com sucesso.")
            }
    )
    @PutMapping("/{idPedido}/status")
    public ResponseEntity<PedidoDTO> updateStatus(@PathVariable("idPedido") Integer idPedido,
                                                  @RequestParam("status") StatusPedido novoStatus) throws RegraDeNegocioException, BancoDeDadosException {
        log.info("Recebida requisição para atualizar status do pedido ID: {} para {}", idPedido, novoStatus);
        PedidoDTO pedidoAtualizado = pedidoService.updateStatus(idPedido, novoStatus);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @Operation(summary = "Excluir um pedido", description = "Remove o registro de um pedido do sistema (se permitido pelas regras de negócio).")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Pedido excluído com sucesso.")
            }
    )
    @DeleteMapping("/{idPedido}")
    public ResponseEntity<Void> delete(@PathVariable("idPedido") Integer idPedido) throws RegraDeNegocioException, BancoDeDadosException {
        log.warn("Recebida requisição para deletar pedido com ID: {}", idPedido);
        pedidoService.delete(idPedido);
        return ResponseEntity.noContent().build();
    }
}