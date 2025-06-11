package br.com.dbc.vemser.lojafashionapi.dto.pedido;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PedidoCreateDTO {
    @NotNull
    @Schema(description = "ID do cliente que está fazendo o pedido", required = true, example = "1")
    private Integer idCliente;

    @Valid
    @NotEmpty(message = "A lista de itens não pode estar vazia.")
    @Schema(description = "Lista de itens do pedido")
    private List<ItemPedidoCreateDTO> itens;
}