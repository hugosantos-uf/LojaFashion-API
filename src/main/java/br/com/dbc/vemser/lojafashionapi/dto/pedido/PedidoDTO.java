package br.com.dbc.vemser.lojafashionapi.dto.pedido;

import br.com.dbc.vemser.lojafashionapi.dto.cliente.ClienteDTO;
import br.com.dbc.vemser.lojafashionapi.enums.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class PedidoDTO {
    @Schema(description = "Identificador único do pedido")
    private Integer idPedido;

    @Schema(description = "Data em que o pedido foi realizado")
    private LocalDate dataPedido;

    @Schema(description = "Status atual do pedido")
    private StatusPedido statusPedido;

    @Schema(description = "Detalhes do cliente que fez o pedido")
    private ClienteDTO cliente;

    @Schema(description = "Valor total do pedido")
    private BigDecimal valorTotalPedido;

    @Schema(description = "Lista de itens do pedido")
    private List<ItemPedidoDTO> itens;
}