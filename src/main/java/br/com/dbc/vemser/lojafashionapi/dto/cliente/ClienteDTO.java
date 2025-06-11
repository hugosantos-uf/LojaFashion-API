package br.com.dbc.vemser.lojafashionapi.dto.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ClienteDTO {

    @Schema(description = "Identificador único do cliente")
    private Integer idCliente;

    @Schema(description = "Nome completo do cliente", example = "João da Silva Sauro")
    private String nomeCompleto;

    @Schema(description = "CPF do cliente (11 dígitos, sem pontos ou traços)", example = "12345678901")
    private String cpf;

    @Schema(description = "Data de nascimento do cliente", example = "1990-01-15")
    private LocalDate dataNascimento;

    @Schema(description = "E-mail do cliente", example = "joao.sauro@example.com")
    private String email;

    @Schema(description = "Telefone de contato do cliente", example = "5562999998888")
    private String telefone;
}