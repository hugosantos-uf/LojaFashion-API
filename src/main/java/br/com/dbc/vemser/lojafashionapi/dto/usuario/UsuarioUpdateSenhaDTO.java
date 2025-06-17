package br.com.dbc.vemser.lojafashionapi.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UsuarioUpdateSenhaDTO {
    @Schema(description = "Senha atual do usuário", required = true)
    @NotBlank
    private String senhaAtual;

    @Schema(description = "Nova senha do usuário", required = true)
    @NotBlank
    @Size(min = 6)
    private String novaSenha;
}