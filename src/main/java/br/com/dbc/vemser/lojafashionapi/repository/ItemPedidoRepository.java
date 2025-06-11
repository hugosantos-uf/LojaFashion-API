package br.com.dbc.vemser.lojafashionapi.repository;

import br.com.dbc.vemser.lojafashionapi.dto.relatorios.RelatorioProdutoVendidoDTO;
import br.com.dbc.vemser.lojafashionapi.entity.ItemPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedidoEntity, Integer> {
    boolean existsByProdutoIdProduto(Integer idProduto);

    @Query("SELECT new br.com.dbc.vemser.lojafashionapi.dto.relatorios.RelatorioProdutoVendidoDTO(" +
            "p.nome, " +
            "c.nome, " +
            "SUM(ip.quantidade), " +
            "SUM(ip.quantidade * ip.precoUnitario)) " +
            "FROM ITEM_PEDIDO ip " +
            "JOIN ip.produto p " +
            "JOIN p.categoria c " +
            "GROUP BY p.nome, c.nome " +
            "ORDER BY SUM(ip.quantidade) DESC")
    List<RelatorioProdutoVendidoDTO> findProdutosMaisVendidos();
}