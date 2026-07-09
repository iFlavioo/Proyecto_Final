package com.bookpoint.usuario.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookpoint.usuario.model.Usuario;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
