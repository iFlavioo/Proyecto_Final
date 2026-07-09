package com.bookpoint.proveedor.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookpoint.proveedor.model.Proveedor;
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
}
