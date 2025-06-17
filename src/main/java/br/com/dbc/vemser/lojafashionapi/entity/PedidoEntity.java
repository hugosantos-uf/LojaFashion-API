
package br.com.dbc.vemser.lojafashionapi.entity;

import br.com.dbc.vemser.lojafashionapi.enums.StatusPedido;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity(name = "PEDIDO")
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PEDIDO")
    private Integer idPedido;

    @Column(name = "VALOR_TOTAL_PEDIDO")
    private BigDecimal valorTotalPedido;

    @Column(name = "DATA_PEDIDO")
    private LocalDate dataPedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_PEDIDO")
    private StatusPedido statusPedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID_CLIENTE")
    private ClienteEntity cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemPedidoEntity> itens;
}