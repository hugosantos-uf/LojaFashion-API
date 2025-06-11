package br.com.dbc.vemser.lojafashionapi.controller;

import br.com.dbc.vemser.lojafashionapi.dto.cliente.ClienteCreateDTO;
import br.com.dbc.vemser.lojafashionapi.dto.cliente.ClienteDTO;
import br.com.dbc.vemser.lojafashionapi.exception.BancoDeDadosException;
import br.com.dbc.vemser.lojafashionapi.exception.RegraDeNegocioException;
import br.com.dbc.vemser.lojafashionapi.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cliente")
@Validated
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cliente", description = "Endpoints para gerenciamento de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(summary = "Criar um novo cliente", description = "Cria um novo cliente no sistema e envia um e-mail de boas-vindas.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso.")
            }
    )
    @PostMapping
    public ResponseEntity<ClienteDTO> create(@Valid @RequestBody ClienteCreateDTO clienteCreateDTO) throws RegraDeNegocioException, BancoDeDadosException {
        log.info("Recebida requisição para criar cliente: {}", clienteCreateDTO.getEmail());
        ClienteDTO clienteCriado = clienteService.create(clienteCreateDTO);
        log.info("Cliente {} (ID: {}) criado com sucesso!", clienteCriado.getNomeCompleto(), clienteCriado.getIdCliente());
        return new ResponseEntity<>(clienteCriado, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista paginada com todos os clientes cadastrados.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso.")})
    @GetMapping
    public ResponseEntity<Page<ClienteDTO>> listAll(@PageableDefault(size = 10, page = 0, sort = {"nomeCompleto"}) Pageable pageable) {
        log.info("Recebida requisição para listar todos os clientes de forma paginada.");
        Page<ClienteDTO> clientes = clienteService.listAll(pageable);
        return ResponseEntity.ok(clientes);
    }
    @Operation(summary = "Buscar cliente por ID", description = "Retorna os detalhes de um cliente específico a partir do seu ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Cliente retornado com sucesso.")
            }
    )
    @GetMapping("/{idCliente}")
    public ResponseEntity<ClienteDTO> getById(@PathVariable("idCliente") Integer idCliente) throws RegraDeNegocioException, BancoDeDadosException {
        log.info("Recebida requisição para buscar cliente com ID: {}", idCliente);
        ClienteDTO cliente = clienteService.getById(idCliente);
        return ResponseEntity.ok(cliente);
    }

    @Operation(summary = "Atualizar um cliente", description = "Atualiza os dados de um cliente existente a partir do seu ID. A senha não é atualizada por este endpoint.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso.")
            }
    )
    @PutMapping("/{idCliente}")
    public ResponseEntity<ClienteDTO> update(@PathVariable("idCliente") Integer idCliente,
                                             @Valid @RequestBody ClienteCreateDTO clienteAtualizarDTO) throws RegraDeNegocioException, BancoDeDadosException {
        log.info("Recebida requisição para atualizar cliente com ID: {}", idCliente);
        ClienteDTO clienteAtualizado = clienteService.update(idCliente, clienteAtualizarDTO);
        return ResponseEntity.ok(clienteAtualizado);
    }

    @Operation(summary = "Excluir um cliente", description = "Remove o registro de um cliente do sistema a partir do seu ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso.")
            }
    )
    @DeleteMapping("/{idCliente}")
    public ResponseEntity<Void> delete(@PathVariable("idCliente") Integer idCliente) throws RegraDeNegocioException, BancoDeDadosException {
        log.warn("Recebida requisição para deletar cliente com ID: {}", idCliente);
        clienteService.delete(idCliente);
        return ResponseEntity.noContent().build();
    }
}