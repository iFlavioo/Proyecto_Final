package com.bookpoint.inventario.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookpoint.inventario.model.Inventario;
import java.util.Optional;
@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    Optional<Inventario> findByProductoIdAndSucursalId(Long productoId, Long sucursalId);
}
