package br.com.dbc.vemser.lojafashionapi.service;

import br.com.dbc.vemser.lojafashionapi.dto.usuario.UsuarioCreateDTO;
import br.com.dbc.vemser.lojafashionapi.dto.usuario.UsuarioDTO;
import br.com.dbc.vemser.lojafashionapi.dto.usuario.UsuarioUpdateSenhaDTO;
import br.com.dbc.vemser.lojafashionapi.entity.CargoEntity;
import br.com.dbc.vemser.lojafashionapi.entity.Usuario;
import br.com.dbc.vemser.lojafashionapi.exception.RegraDeNegocioException;
import br.com.dbc.vemser.lojafashionapi.repository.CargoRepository;
import br.com.dbc.vemser.lojafashionapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CargoRepository cargoRepository;
    private final ObjectMapper objectMapper;

    public Optional<Usuario> findByLogin(String login){
        return usuarioRepository.findByLogin(login);
    }

    public UsuarioDTO criarUsuario(UsuarioCreateDTO usuarioCreateDTO) throws Exception {
        if(usuarioRepository.findByLogin(usuarioCreateDTO.getLogin()).isPresent()){
            throw new Exception("Login já cadastrado!");
        }

        Usuario usuario = new Usuario();
        usuario.setLogin(usuarioCreateDTO.getLogin());
        usuario.setSenha(passwordEncoder.encode(usuarioCreateDTO.getSenha()));
        usuario.setAtivo(true);

        Set<CargoEntity> cargos = usuarioCreateDTO.getCargos().stream()
                .map(idCargo -> cargoRepository.findById(idCargo)
                        .orElseThrow(() -> new RuntimeException("Cargo não encontrado: " + idCargo)))
                .collect(Collectors.toSet());

        usuario.setCargos(cargos);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return objectMapper.convertValue(usuarioSalvo, UsuarioDTO.class);
    }

    public Integer getIdLoggedUser() {
        return Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

    public Usuario getLoggedUser() throws RegraDeNegocioException {
        return findById(getIdLoggedUser());
    }

    public UsuarioDTO getLoggedUserDTO() throws RegraDeNegocioException {
        return objectMapper.convertValue(getLoggedUser(), UsuarioDTO.class);
    }

    public void trocarSenha(UsuarioUpdateSenhaDTO senhas) throws RegraDeNegocioException {
        Usuario usuario = getLoggedUser();

        if (!passwordEncoder.matches(senhas.getSenhaAtual(), usuario.getPassword())) {
            throw new RegraDeNegocioException("Senha atual incorreta.");
        }

        String novaSenhaCriptografada = passwordEncoder.encode(senhas.getNovaSenha());
        usuario.setSenha(novaSenhaCriptografada);
        usuarioRepository.save(usuario);
    }

    public void desativarUsuario(Integer idUsuario) throws RegraDeNegocioException {
        Usuario usuario = findById(idUsuario);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    public Usuario findById(Integer id) throws RegraDeNegocioException {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado."));
    }
}
