package br.com.dbc.vemser.lojafashionapi.dto.categoria;

import io.swagger.v3.oas.annotations.media.Schema;
import javax .validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoriaCreateDTO {

    @NotBlank(message = "O nome da categoria não pode ser vazio ou nulo.")
    @Size(min = 2, max = 100, message = "O nome da categoria deve ter entre 2 e 100 caracteres.")
    @Schema(description = "Nome da categoria", example = "Roupas Femininas", required = true)
    private String nome;

    @Size(max = 255, message = "A descrição da categoria deve ter no máximo 255 caracteres.")
    @Schema(description = "Descrição opcional da categoria", example = "Vestidos, Blusas, Saias, etc.")
    private String descricao;
}