package br.com.dbc.vemser.lojafashionapi.service;

import br.com.dbc.vemser.lojafashionapi.dto.cliente.ClienteCreateDTO;
import br.com.dbc.vemser.lojafashionapi.dto.cliente.ClienteDTO;
import br.com.dbc.vemser.lojafashionapi.entity.ClienteEntity;
import br.com.dbc.vemser.lojafashionapi.exception.RegraDeNegocioException;
import br.com.dbc.vemser.lojafashionapi.repository.ClienteRepository;
import br.com.dbc.vemser.lojafashionapi.repository.PedidoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository; // Injetado para validação
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public ClienteDTO create(ClienteCreateDTO clienteCreateDTO) throws RegraDeNegocioException {
        log.info("Criando cliente...");

        if (clienteRepository.findByCpf(clienteCreateDTO.getCpf()).isPresent()) {
            throw new RegraDeNegocioException("CPF já cadastrado.");
        }
        if (clienteRepository.findByEmail(clienteCreateDTO.getEmail()).isPresent()) {
            throw new RegraDeNegocioException("E-mail já cadastrado.");
        }

        ClienteEntity clienteEntity = objectMapper.convertValue(clienteCreateDTO, ClienteEntity.class);

        ClienteEntity clienteCriado = clienteRepository.save(clienteEntity);
        log.info("Cliente '{}' (ID: {}) criado com sucesso.", clienteCriado.getNomeCompleto(), clienteCriado.getIdCliente());

        ClienteDTO clienteDTO = objectMapper.convertValue(clienteCriado, ClienteDTO.class);

        Map<String, Object> dadosEmail = Map.of(
                "nomeCliente", clienteDTO.getNomeCompleto(),
                "idCliente", clienteDTO.getIdCliente().toString()
        );
        emailService.sendEmail("Bem-vindo(a) à Loja Fashion!", "cliente-bemvindo-template.ftl", dadosEmail, clienteDTO.getEmail());

        return clienteDTO;
    }

    public Page<ClienteDTO> listAll(Pageable pageable) {
        return clienteRepository.findAll(pageable)
                .map(cliente -> objectMapper.convertValue(cliente, ClienteDTO.class));
    }

    public ClienteDTO getById(Integer idCliente) throws RegraDeNegocioException {
        ClienteEntity clienteEntity = findClienteById(idCliente);
        return objectMapper.convertValue(clienteEntity, ClienteDTO.class);
    }

    public ClienteDTO update(Integer idCliente, ClienteCreateDTO clienteAtualizarDTO) throws RegraDeNegocioException {
        log.info("Atualizando cliente ID: {}", idCliente);
        ClienteEntity clienteRecuperado = findClienteById(idCliente);

        clienteRepository.findByCpf(clienteAtualizarDTO.getCpf()).ifPresent(cliente -> {
            if (!cliente.getIdCliente().equals(idCliente)) {
                throw new RuntimeException(new RegraDeNegocioException("CPF já cadastrado para outro cliente."));
            }
        });
        clienteRepository.findByEmail(clienteAtualizarDTO.getEmail()).ifPresent(cliente -> {
            if (!cliente.getIdCliente().equals(idCliente)) {
                throw new RuntimeException(new RegraDeNegocioException("E-mail já cadastrado para outro cliente."));
            }
        });

        clienteRecuperado.setNomeCompleto(clienteAtualizarDTO.getNomeCompleto());
        clienteRecuperado.setCpf(clienteAtualizarDTO.getCpf());
        clienteRecuperado.setDataNascimento(clienteAtualizarDTO.getDataNascimento());
        clienteRecuperado.setEmail(clienteAtualizarDTO.getEmail());
        clienteRecuperado.setTelefone(clienteAtualizarDTO.getTelefone());

        ClienteEntity clienteAtualizado = clienteRepository.save(clienteRecuperado);
        log.info("Cliente ID: {} atualizado com sucesso!", idCliente);
        return objectMapper.convertValue(clienteAtualizado, ClienteDTO.class);
    }

    public void delete(Integer idCliente) throws RegraDeNegocioException {
        log.warn("Deletando cliente ID: {}", idCliente);
        findClienteById(idCliente);

        if (pedidoRepository.existsByClienteIdCliente(idCliente)) {
            throw new RegraDeNegocioException("Não é possível excluir o cliente, pois existem pedidos associados a ele.");
        }

        clienteRepository.deleteById(idCliente);
        log.info("Cliente ID: {} deletado com sucesso!", idCliente);
    }

    public ClienteEntity findClienteById(Integer idCliente) throws RegraDeNegocioException {
        return clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RegraDeNegocioException("Cliente não encontrado com o ID: " + idCliente));
    }
}
