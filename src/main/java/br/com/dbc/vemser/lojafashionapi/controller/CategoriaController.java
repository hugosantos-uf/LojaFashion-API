package br.com.dbc.vemser.lojafashionapi.controller;

import br.com.dbc.vemser.lojafashionapi.dto.categoria.CategoriaCreateDTO;
import br.com.dbc.vemser.lojafashionapi.dto.categoria.CategoriaDTO;
import br.com.dbc.vemser.lojafashionapi.exception.*;
import br.com.dbc.vemser.lojafashionapi.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoria")
@Validated
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Categoria", description = "Endpoints para gerenciamento de categorias de produtos")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Operation(summary = "Criar uma nova categoria", description = "Cria uma nova categoria de produtos no sistema.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso.")
            }
    )
    @PostMapping
    public ResponseEntity<CategoriaDTO> create(@Valid @RequestBody CategoriaCreateDTO categoriaCreateDTO) throws RegraDeNegocioException, BancoDeDadosException {
        log.info("Criando categoria...");
        CategoriaDTO categoriaCriada = categoriaService.create(categoriaCreateDTO);
        log.info("Categoria '{}' (ID: {}) criada com sucesso!", categoriaCriada.getNome(), categoriaCriada.getIdCategoria());
        return new ResponseEntity<>(categoriaCriada, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todas as categorias", description = "Retorna uma lista com todas as categorias cadastradas.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso.")})
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listAll() throws BancoDeDadosException {
        log.info("Listando todas as categorias...");
        List<CategoriaDTO> categorias = categoriaService.listAll();
        log.info("Total de categorias listadas: {}", categorias.size());
        return ResponseEntity.ok(categorias);
    }

    @Operation(summary = "Buscar categoria por ID", description = "Retorna os detalhes de uma categoria específica a partir do seu ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Categoria retornada com sucesso.")
            }
    )
    @GetMapping("/{idCategoria}")
    public ResponseEntity<CategoriaDTO> getById(@PathVariable("idCategoria") Integer idCategoria) throws RegraDeNegocioException, BancoDeDadosException {
        log.info("Buscando categoria com ID: {}", idCategoria);
        CategoriaDTO categoria = categoriaService.getById(idCategoria);
        log.info("Categoria encontrada: ID {}", categoria.getIdCategoria());
        return ResponseEntity.ok(categoria);
    }

    @Operation(summary = "Atualizar uma categoria", description = "Atualiza os dados de uma categoria existente a partir do seu ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso.")
            }
    )
    @PutMapping("/{idCategoria}")
    public ResponseEntity<CategoriaDTO> update(@PathVariable("idCategoria") Integer idCategoria,
                                               @Valid @RequestBody CategoriaCreateDTO categoriaAtualizarDTO) throws RegraDeNegocioException, BancoDeDadosException {
        log.info("Atualizando categoria com ID: {}", idCategoria);
        CategoriaDTO categoriaAtualizada = categoriaService.update(idCategoria, categoriaAtualizarDTO);
        log.info("Categoria com ID {} atualizada com sucesso!", idCategoria);
        return ResponseEntity.ok(categoriaAtualizada);
    }

    @Operation(summary = "Excluir uma categoria", description = "Remove o registro de uma categoria do sistema a partir do seu ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso.")
            }
    )
    @DeleteMapping("/{idCategoria}")
    public ResponseEntity<Void> delete(@PathVariable("idCategoria") Integer idCategoria) throws RegraDeNegocioException, BancoDeDadosException {
        log.warn("Deletando categoria com ID: {}", idCategoria);
        categoriaService.delete(idCategoria);
        log.info("Categoria com ID {} deletada com sucesso!", idCategoria);
        return ResponseEntity.noContent().build();
    }
}