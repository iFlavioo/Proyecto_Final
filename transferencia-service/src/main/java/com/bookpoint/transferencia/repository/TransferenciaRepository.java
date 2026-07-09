package com.bookpoint.transferencia.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookpoint.transferencia.model.Transferencia;
@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
}
