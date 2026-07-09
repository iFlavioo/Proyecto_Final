package com.bookpoint.resena.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookpoint.resena.model.Resena;
@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
}
