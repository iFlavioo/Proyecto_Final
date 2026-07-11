package com.bookpoint.usuario.service;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bookpoint.usuario.model.Usuario;
import com.bookpoint.usuario.repository.UsuarioRepository;
@Service
public class UsuarioService {
    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);
    private static final String ROLES_VALIDOS = "ADMIN|CLIENTE|VENDEDOR|LOGISTICA|BODEGA";

    @Autowired private UsuarioRepository usuarioRepository;

    private void validarUsuario(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            log.error("nombre no válido: no puede estar vacío ni en blanco");
            throw new IllegalArgumentException("El nombre no puede estar vacío ni en blanco");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            log.error("email no válido: no puede estar vacío ni en blanco");
            throw new IllegalArgumentException("El email no puede estar vacío ni en blanco");
        }
        if (usuario.getEmail() == null || !usuario.getEmail().contains("@")) {
            log.error("email no válido: {}", usuario.getEmail());
            throw new IllegalArgumentException("El email debe contener un @");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            log.error("password no válido: no puede estar vacío ni en blanco");
            throw new IllegalArgumentException("El password no puede estar vacío ni en blanco");
        }
        if (usuario.getRol() == null || !usuario.getRol().matches(ROLES_VALIDOS)) {
            log.error("rol no válido: {}", usuario.getRol());
            throw new IllegalArgumentException("Rol invalido. Use: ADMIN, CLIENTE, VENDEDOR, LOGISTICA o BODEGA");
        }
    }

    public Usuario guardarUsuario(Usuario usuario) {
        validarUsuario(usuario);
        return usuarioRepository.save(usuario);
    }
    public List<Usuario> listarUsuarios() { return usuarioRepository.findAll(); }
    public Optional<Usuario> obtenerUsuarioPorId(Long id) { return usuarioRepository.findById(id); }
    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        validarUsuario(usuario);
        Usuario existente = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No existe usuario con id: " + id));
        existente.setNombre(usuario.getNombre());
        existente.setEmail(usuario.getEmail());
        existente.setPassword(usuario.getPassword());
        existente.setRol(usuario.getRol());
        return usuarioRepository.save(existente);
    }
    public void eliminarUsuario(Long id) { usuarioRepository.deleteById(id); }
}
