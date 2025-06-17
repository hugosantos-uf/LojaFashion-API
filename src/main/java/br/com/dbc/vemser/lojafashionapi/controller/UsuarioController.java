package br.com.dbc.vemser.lojafashionapi.controller;

import br.com.dbc.vemser.lojafashionapi.dto.usuario.UsuarioCreateDTO;
import br.com.dbc.vemser.lojafashionapi.dto.usuario.UsuarioDTO;
import br.com.dbc.vemser.lojafashionapi.dto.usuario.UsuarioUpdateSenhaDTO;
import br.com.dbc.vemser.lojafashionapi.exception.RegraDeNegocioException;
import br.com.dbc.vemser.lojafashionapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/usuario")
@Validated
@RequiredArgsConstructor
@Tag(name = "Usuário", description = "Endpoints para gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> criarUsuario(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) throws Exception {
        UsuarioDTO usuarioCriado = usuarioService.criarUsuario(usuarioCreateDTO);
        return new ResponseEntity<>(usuarioCriado, HttpStatus.CREATED);
    }

    @GetMapping("/logado")
    public ResponseEntity<UsuarioDTO> getLoggedUser() throws RegraDeNegocioException {
        return ResponseEntity.ok(usuarioService.getLoggedUserDTO());
    }

    @PutMapping("/trocar-senha")
    public ResponseEntity<Void> trocarSenha(@Valid @RequestBody UsuarioUpdateSenhaDTO dto) throws RegraDeNegocioException {
        usuarioService.trocarSenha(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> desativarUsuario(@PathVariable Integer idUsuario) throws RegraDeNegocioException {
        usuarioService.desativarUsuario(idUsuario);
        return ResponseEntity.noContent().build();
    }
}