package com.bookpoint.pedido.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookpoint.pedido.model.Pedido;
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
