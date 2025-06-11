package br.com.dbc.vemser.lojafashionapi.dto.relatorios;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioProdutoVendidoDTO {
    private String nomeProduto;
    private String categoriaProduto;
    private Long quantidadeTotalVendida;
    private BigDecimal receitaTotalGerada;
}