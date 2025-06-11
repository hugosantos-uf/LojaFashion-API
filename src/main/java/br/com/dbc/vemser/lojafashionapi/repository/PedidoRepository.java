package br.com.dbc.vemser.lojafashionapi.repository;

import br.com.dbc.vemser.lojafashionapi.dto.relatorios.RelatorioPedidoClienteDTO;
import br.com.dbc.vemser.lojafashionapi.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Integer> {
    List<PedidoEntity> findByCliente_IdCliente(Integer idCliente);

    boolean existsByClienteIdCliente(Integer idCliente);

    @Query("SELECT new br.com.dbc.vemser.lojafashionapi.dto.relatorios.RelatorioPedidoClienteDTO(" +
            "c.nomeCompleto, " +
            "c.email, " +
            "p.idPedido, " +
            "p.valorTotalPedido, " +
            "p.dataPedido) " +
            "FROM PEDIDO p " +
            "JOIN p.cliente c " +
            "WHERE (:idCliente IS NULL OR c.idCliente = :idCliente)")
    List<RelatorioPedidoClienteDTO> findPedidosByCliente(@Param("idCliente") Integer idCliente);
}
