package br.com.dbc.vemser.lojafashionapi.dto.usuario;

import br.com.dbc.vemser.lojafashionapi.entity.CargoEntity;
import lombok.Data;

import java.util.Set;

@Data
public class UsuarioDTO {
    private Integer idUsuario;
    private String login;
    private Set<CargoEntity> cargos;
}