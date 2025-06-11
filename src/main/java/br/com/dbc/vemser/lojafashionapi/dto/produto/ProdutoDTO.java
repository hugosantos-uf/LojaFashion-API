package br.com.dbc.vemser.lojafashionapi.dto.produto;

import br.com.dbc.vemser.lojafashionapi.dto.categoria.CategoriaDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class ProdutoDTO extends ProdutoCreateDTO {

    @Schema(description = "Identificador único do produto")
    private Integer idProduto;

    @Schema(description = "Detalhes da categoria do produto")
    private CategoriaDTO categoria;
}