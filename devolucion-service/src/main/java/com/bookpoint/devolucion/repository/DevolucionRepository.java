package com.bookpoint.devolucion.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookpoint.devolucion.model.Devolucion;
@Repository
public interface DevolucionRepository extends JpaRepository<Devolucion, Long> {
}
