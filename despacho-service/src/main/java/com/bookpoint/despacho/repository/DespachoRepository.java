package com.bookpoint.despacho.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookpoint.despacho.model.Despacho;
@Repository
public interface DespachoRepository extends JpaRepository<Despacho, Long> {
}
