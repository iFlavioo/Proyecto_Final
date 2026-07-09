package com.bookpoint.usuario.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bookpoint.usuario.model.Usuario;
import com.bookpoint.usuario.repository.UsuarioRepository;
@Service
public class UsuarioService {
    @Autowired private UsuarioRepository usuarioRepository;

    public Usuario guardarUsuario(Usuario usuario) { return usuarioRepository.save(usuario); }
    public List<Usuario> listarUsuarios() { return usuarioRepository.findAll(); }
    public Optional<Usuario> obtenerUsuarioPorId(Long id) { return usuarioRepository.findById(id); }
    public Usuario actualizarUsuario(Long id, Usuario usuario) {
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
