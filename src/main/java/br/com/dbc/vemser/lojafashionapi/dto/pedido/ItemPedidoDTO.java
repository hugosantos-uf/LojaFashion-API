package br.com.dbc.vemser.lojafashionapi.dto.pedido;

import br.com.dbc.vemser.lojafashionapi.dto.produto.ProdutoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemPedidoDTO {
    @Schema(description = "Produto comprado")
    private ProdutoDTO produto;

    @Schema(description = "Quantidade comprada")
    private Integer quantidade;

    @Schema(description = "Preço unitário no momento da compra")
    private BigDecimal precoUnitario;
}