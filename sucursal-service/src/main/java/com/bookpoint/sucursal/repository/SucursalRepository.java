package com.bookpoint.sucursal.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookpoint.sucursal.model.Sucursal;
@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
}
