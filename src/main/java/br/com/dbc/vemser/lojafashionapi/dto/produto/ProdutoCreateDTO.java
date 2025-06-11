package br.com.dbc.vemser.lojafashionapi.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class ProdutoCreateDTO {

    @NotNull(message = "O ID da categoria não pode ser nulo.")
    @Schema(description = "ID da categoria à qual o produto pertence", required = true, example = "1")
    private Integer idCategoria;

    @NotBlank(message = "O nome do produto não pode ser vazio ou nulo.")
    @Size(min = 2, max = 150, message = "O nome do produto deve ter entre 2 e 150 caracteres.")
    @Schema(description = "Nome do produto", required = true, example = "Vestido Floral de Verão")
    private String nome;

    @Size(max = 500, message = "A descrição do produto deve ter no máximo 500 caracteres.")
    @Schema(description = "Descrição detalhada do produto", example = "Vestido leve e confortável, perfeito para dias quentes.")
    private String descricao;

    @NotNull(message = "O preço não pode ser nulo.")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
    @Digits(integer = 8, fraction = 2, message = "O preço deve ter no máximo 8 dígitos inteiros e 2 casas decimais.")
    @Schema(description = "Preço do produto", required = true, example = "129.99")
    private BigDecimal preco;

    @Size(max = 20, message = "O tamanho deve ter no máximo 20 caracteres.")
    @Schema(description = "Tamanho do produto (ex: P, M, G, 38, Único)", example = "M")
    private String tamanho;

    @Size(max = 50, message = "A cor deve ter no máximo 50 caracteres.")
    @Schema(description = "Cor do produto", example = "Azul Marinho com Estampa Floral Branca")
    private String cor;

    @NotNull(message = "A quantidade em estoque não pode ser nula.")
    @Min(value = 0, message = "A quantidade em estoque não pode ser negativa.")
    @Schema(description = "Quantidade do produto em estoque", required = true, example = "50")
    private Integer quantidadeEstoque;
}