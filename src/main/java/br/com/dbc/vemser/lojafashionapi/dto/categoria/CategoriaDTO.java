package br.com.dbc.vemser.lojafashionapi.dto.categoria;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CategoriaDTO extends CategoriaCreateDTO {

    @Schema(description = "Identificador único da categoria")
    private Integer idCategoria;
}