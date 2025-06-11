package br.com.dbc.vemser.lojafashionapi.dto.relatorios;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioPedidoClienteDTO {
    private String nomeCliente;
    private String emailCliente;
    private Integer idPedido;
    private BigDecimal valorTotalPedido;
    private LocalDate dataPedido;
}