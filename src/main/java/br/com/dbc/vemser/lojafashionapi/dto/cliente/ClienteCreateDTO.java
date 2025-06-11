package br.com.dbc.vemser.lojafashionapi.dto.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class ClienteCreateDTO {

    @NotBlank(message = "O nome completo não pode ser vazio ou nulo.")
    @Size(min = 3, max = 255, message = "O nome completo deve ter entre 3 e 255 caracteres.")
    @Schema(description = "Nome completo do cliente", required = true, example = "João da Silva Sauro")
    private String nomeCompleto;

    @NotBlank(message = "O CPF não pode ser vazio ou nulo.")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
    @Schema(description = "CPF do cliente (11 dígitos, sem pontos ou traços)", required = true, example = "12345678901")
    private String cpf;

    @NotNull(message = "A data de nascimento não pode ser nula.")
    @Past(message = "A data de nascimento deve ser uma data no passado.")
    @Schema(description = "Data de nascimento do cliente", required = true, example = "1990-01-15")
    private LocalDate dataNascimento;

    @NotBlank(message = "O e-mail não pode ser vazio ou nulo.")
    @Email(message = "O formato do e-mail é inválido.")
    @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres.")
    @Schema(description = "E-mail do cliente", required = true, example = "joao.sauro@example.com")
    private String email;

    @Size(max = 15, message = "O telefone deve ter no máximo 15 caracteres.")
    @Pattern(regexp = "^[+]?[0-9]*$", message = "O telefone deve conter apenas números e opcionalmente o prefixo '+'.")
    @Schema(description = "Telefone de contato do cliente (opcional)", example = "5562999998888")
    private String telefone;

    @NotBlank(message = "A senha não pode ser vazia ou nula.")
    @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres.")
    @Schema(description = "Senha de acesso do cliente (entre 6 e 20 caracteres)", required = true, example = "senha@123")
    private String senha;
}