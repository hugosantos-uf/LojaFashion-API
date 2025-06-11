package br.com.dbc.vemser.lojafashionapi.service;

import br.com.dbc.vemser.lojafashionapi.dto.categoria.CategoriaDTO;
import br.com.dbc.vemser.lojafashionapi.dto.produto.ProdutoCreateDTO;
import br.com.dbc.vemser.lojafashionapi.dto.produto.ProdutoDTO;
import br.com.dbc.vemser.lojafashionapi.entity.CategoriaEntity;
import br.com.dbc.vemser.lojafashionapi.entity.ProdutoEntity;
import br.com.dbc.vemser.lojafashionapi.exception.RegraDeNegocioException;
import br.com.dbc.vemser.lojafashionapi.repository.ItemPedidoRepository;
import br.com.dbc.vemser.lojafashionapi.repository.PedidoRepository;
import br.com.dbc.vemser.lojafashionapi.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaService categoriaService;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ObjectMapper objectMapper;

    public ProdutoDTO create(ProdutoCreateDTO produtoCreateDTO) throws RegraDeNegocioException {
        log.info("Criando produto...");
        CategoriaEntity categoria = categoriaService.findCategoriaById(produtoCreateDTO.getIdCategoria());

        ProdutoEntity produtoEntity = objectMapper.convertValue(produtoCreateDTO, ProdutoEntity.class);
        produtoEntity.setCategoria(categoria); // Associa a entidade da categoria

        ProdutoEntity produtoCriado = produtoRepository.save(produtoEntity);
        log.info("Produto '{}' criado com sucesso!", produtoCriado.getNome());
        return produtoToProdutoDTO(produtoCriado);
    }

    public Page<ProdutoDTO> listAll(Pageable pageable) {
        Page<ProdutoEntity> produtosPage = produtoRepository.findAll(pageable);
        return produtosPage.map(this::produtoToProdutoDTO);
    }

    public ProdutoDTO getById(Integer idProduto) throws RegraDeNegocioException {
        ProdutoEntity produtoEntity = findProdutoById(idProduto);
        return produtoToProdutoDTO(produtoEntity);
    }

    public List<ProdutoDTO> getByCategoriaId(Integer idCategoria) throws RegraDeNegocioException {
        // Valida se a categoria existe
        categoriaService.findCategoriaById(idCategoria);
        return produtoRepository.findByCategoria_IdCategoria(idCategoria).stream()
                .map(this::produtoToProdutoDTO)
                .collect(Collectors.toList());
    }

    public ProdutoDTO update(Integer idProduto, ProdutoCreateDTO produtoAtualizarDTO) throws RegraDeNegocioException {
        log.info("Atualizando produto ID: {}", idProduto);
        ProdutoEntity produtoRecuperado = findProdutoById(idProduto);
        CategoriaEntity categoria = categoriaService.findCategoriaById(produtoAtualizarDTO.getIdCategoria());

        produtoRecuperado.setCategoria(categoria);
        produtoRecuperado.setNome(produtoAtualizarDTO.getNome());
        produtoRecuperado.setDescricao(produtoAtualizarDTO.getDescricao());
        produtoRecuperado.setPreco(produtoAtualizarDTO.getPreco());
        produtoRecuperado.setTamanho(produtoAtualizarDTO.getTamanho());
        produtoRecuperado.setCor(produtoAtualizarDTO.getCor());
        produtoRecuperado.setQuantidadeEstoque(produtoAtualizarDTO.getQuantidadeEstoque());

        ProdutoEntity produtoAtualizado = produtoRepository.save(produtoRecuperado);
        log.info("Produto ID: {} atualizado com sucesso!", idProduto);
        return produtoToProdutoDTO(produtoAtualizado);
    }

    public void delete(Integer idProduto) throws RegraDeNegocioException {
        log.warn("Deletando produto ID: {}", idProduto);
        findProdutoById(idProduto);

        if (itemPedidoRepository.existsByProdutoIdProduto(idProduto)) {
            throw new RegraDeNegocioException("Não é possível excluir o produto, pois existem pedidos associados a ele.");
        }

        produtoRepository.deleteById(idProduto);
        log.info("Produto ID: {} deletado com sucesso!", idProduto);
    }

    public ProdutoEntity findProdutoById(Integer idProduto) throws RegraDeNegocioException {
        return produtoRepository.findById(idProduto)
                .orElseThrow(() -> new RegraDeNegocioException("Produto não encontrado com o ID: " + idProduto));
    }

    ProdutoDTO produtoToProdutoDTO(ProdutoEntity produtoEntity) {
        ProdutoDTO produtoDTO = objectMapper.convertValue(produtoEntity, ProdutoDTO.class);
        produtoDTO.setCategoria(objectMapper.convertValue(produtoEntity.getCategoria(), CategoriaDTO.class));
        produtoDTO.setIdCategoria(produtoEntity.getCategoria().getIdCategoria());
        return produtoDTO;
    }
}
