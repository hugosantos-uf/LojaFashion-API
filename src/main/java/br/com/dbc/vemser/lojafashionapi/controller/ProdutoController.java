package br.com.dbc.vemser.lojafashionapi.controller;

import br.com.dbc.vemser.lojafashionapi.dto.produto.ProdutoCreateDTO;
import br.com.dbc.vemser.lojafashionapi.dto.produto.ProdutoDTO;
import br.com.dbc.vemser.lojafashionapi.exception.BancoDeDadosException;
import br.com.dbc.vemser.lojafashionapi.exception.RegraDeNegocioException;
import br.com.dbc.vemser.lojafashionapi.service.ProdutoService;
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
@RequestMapping("/produto")
@Validated
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Produto", description = "Endpoints para gerenciamento de produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Operation(summary = "Criar um novo produto", description = "Cria um novo produto no sistema, associado a uma categoria existente.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Produto criado com sucesso.")
            }
    )
    @PostMapping
    public ResponseEntity<ProdutoDTO> create(@Valid @RequestBody ProdutoCreateDTO produtoCreateDTO) throws RegraDeNegocioException, BancoDeDadosException {
        log.info("Criando produto...");
        ProdutoDTO produtoCriado = produtoService.create(produtoCreateDTO);
        log.info("Produto '{}' (ID: {}) criado com sucesso!", produtoCriado.getNome(), produtoCriado.getIdProduto());
        return new ResponseEntity<>(produtoCriado, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todos os produtos", description = "Retorna uma lista paginada com todos os produtos cadastrados.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso.")})
    @GetMapping
    public ResponseEntity<Page<ProdutoDTO>> listAll(@PageableDefault(size = 10, page = 0, sort = {"nome"}) Pageable pageable) throws RegraDeNegocioException {
        log.info("Listando todos os produtos de forma paginada...");
        Page<ProdutoDTO> produtos = produtoService.listAll(pageable);
        return ResponseEntity.ok(produtos);
    }

    @Operation(summary = "Buscar produto por ID", description = "Retorna os detalhes de um produto específico a partir do seu ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Produto retornado com sucesso.")
            }
    )
    @GetMapping("/{idProduto}")
    public ResponseEntity<ProdutoDTO> getById(@PathVariable("idProduto") Integer idProduto) throws RegraDeNegocioException, BancoDeDadosException {
        log.info("Buscando produto com ID: {}", idProduto);
        ProdutoDTO produto = produtoService.getById(idProduto);
        log.info("Produto encontrado: ID {}", produto.getIdProduto());
        return ResponseEntity.ok(produto);
    }

    @Operation(summary = "Listar produtos por categoria", description = "Retorna uma lista de produtos pertencentes a uma categoria específica.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Lista de produtos da categoria retornada com sucesso.")
            }
    )
    @GetMapping("/por-categoria/{idCategoria}")
    public ResponseEntity<List<ProdutoDTO>> getByCategoriaId(@PathVariable("idCategoria") Integer idCategoria) throws RegraDeNegocioException, BancoDeDadosException {
        log.info("Buscando produtos da categoria ID: {}", idCategoria);
        List<ProdutoDTO> produtos = produtoService.getByCategoriaId(idCategoria);
        log.info("Total de produtos encontrados para a categoria {}: {}", idCategoria, produtos.size());
        return ResponseEntity.ok(produtos);
    }

    @Operation(summary = "Atualizar um produto", description = "Atualiza os dados de um produto existente a partir do seu ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso.")
            }
    )
    @PutMapping("/{idProduto}")
    public ResponseEntity<ProdutoDTO> update(@PathVariable("idProduto") Integer idProduto,
                                             @Valid @RequestBody ProdutoCreateDTO produtoAtualizarDTO) throws RegraDeNegocioException, BancoDeDadosException {
        log.info("Atualizando produto com ID: {}", idProduto);
        ProdutoDTO produtoAtualizado = produtoService.update(idProduto, produtoAtualizarDTO);
        log.info("Produto com ID {} atualizado com sucesso!", idProduto);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @Operation(summary = "Excluir um produto", description = "Remove o registro de um produto do sistema a partir do seu ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso.")
            }
    )
    @DeleteMapping("/{idProduto}")
    public ResponseEntity<Void> delete(@PathVariable("idProduto") Integer idProduto) throws RegraDeNegocioException, BancoDeDadosException {
        log.warn("Deletando produto com ID: {}", idProduto);
        produtoService.delete(idProduto);
        log.info("Produto com ID {} deletado com sucesso!", idProduto);
        return ResponseEntity.noContent().build();
    }
}