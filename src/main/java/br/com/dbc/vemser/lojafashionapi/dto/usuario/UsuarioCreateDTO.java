package br.com.dbc.vemser.lojafashionapi.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UsuarioCreateDTO {
    @Schema(description = "Login do usuário", example = "user.marketing")
    @NotBlank
    private String login;

    @Schema(description = "Senha do usuário")
    @NotBlank
    @Size(min = 6)
    private String senha;

    @Schema(description = "IDs dos cargos a serem atribuídos", example = "[3]") // Ex: 1=ADMIN, 2=CLIENTE, 3=MARKETING
    @NotEmpty
    private Set<Integer> cargos;
}