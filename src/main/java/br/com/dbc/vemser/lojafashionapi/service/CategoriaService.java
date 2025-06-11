package br.com.dbc.vemser.lojafashionapi.service;

import br.com.dbc.vemser.lojafashionapi.dto.categoria.CategoriaCreateDTO;
import br.com.dbc.vemser.lojafashionapi.dto.categoria.CategoriaDTO;
import br.com.dbc.vemser.lojafashionapi.entity.CategoriaEntity;
import br.com.dbc.vemser.lojafashionapi.exception.RegraDeNegocioException;
import br.com.dbc.vemser.lojafashionapi.repository.CategoriaRepository;
import br.com.dbc.vemser.lojafashionapi.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final ObjectMapper objectMapper;

    public CategoriaDTO create(CategoriaCreateDTO categoriaCreateDTO) throws RegraDeNegocioException {
        if (categoriaRepository.findByNome(categoriaCreateDTO.getNome()).isPresent()) {
            throw new RegraDeNegocioException("Já existe uma categoria com este nome.");
        }
        CategoriaEntity categoriaEntity = objectMapper.convertValue(categoriaCreateDTO, CategoriaEntity.class);
        CategoriaEntity categoriaCriada = categoriaRepository.save(categoriaEntity);
        return objectMapper.convertValue(categoriaCriada, CategoriaDTO.class);
    }

    public List<CategoriaDTO> listAll() {
        return categoriaRepository.findAll().stream()
                .map(categoria -> objectMapper.convertValue(categoria, CategoriaDTO.class))
                .collect(Collectors.toList());
    }

    public CategoriaDTO getById(Integer idCategoria) throws RegraDeNegocioException {
        CategoriaEntity categoriaEntity = findCategoriaById(idCategoria);
        return objectMapper.convertValue(categoriaEntity, CategoriaDTO.class);
    }

    public CategoriaDTO update(Integer idCategoria, CategoriaCreateDTO categoriaAtualizarDTO) throws RegraDeNegocioException {
        CategoriaEntity categoriaRecuperada = findCategoriaById(idCategoria);

        categoriaRepository.findByNome(categoriaAtualizarDTO.getNome())
                .ifPresent(categoria -> {
                    if (!categoria.getIdCategoria().equals(idCategoria)) {
                        try {
                            throw new RegraDeNegocioException("Já existe outra categoria com o nome informado.");
                        } catch (RegraDeNegocioException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        categoriaRecuperada.setNome(categoriaAtualizarDTO.getNome());
        categoriaRecuperada.setDescricao(categoriaAtualizarDTO.getDescricao());

        return objectMapper.convertValue(categoriaRepository.save(categoriaRecuperada), CategoriaDTO.class);
    }

    public void delete(Integer idCategoria) throws RegraDeNegocioException {
        findCategoriaById(idCategoria);
        if (produtoRepository.existsByCategoria_IdCategoria(idCategoria)) {
            throw new RegraDeNegocioException("Não é possível excluir a categoria, pois existem produtos associados a ela.");
        }
        categoriaRepository.deleteById(idCategoria);
    }

    public CategoriaEntity findCategoriaById(Integer idCategoria) throws RegraDeNegocioException {
        return categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RegraDeNegocioException("Categoria não encontrada com o ID: " + idCategoria));
    }
}
