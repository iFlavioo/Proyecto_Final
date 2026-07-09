package com.bookpoint.descuento.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookpoint.descuento.model.Descuento;
@Repository
public interface DescuentoRepository extends JpaRepository<Descuento, Long> {
}
