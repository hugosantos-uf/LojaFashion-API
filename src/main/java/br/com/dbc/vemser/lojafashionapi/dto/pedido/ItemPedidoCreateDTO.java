package br.com.dbc.vemser.lojafashionapi.dto.pedido;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class ItemPedidoCreateDTO {
    @NotNull
    @Schema(description = "ID do produto a ser comprado", required = true, example = "1")
    private Integer idProduto;

    @NotNull
    @Positive(message = "A quantidade deve ser um valor positivo.")
    @Schema(description = "Quantidade do produto", required = true, example = "2")
    private Integer quantidade;
}