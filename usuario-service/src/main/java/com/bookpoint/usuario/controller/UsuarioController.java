package com.bookpoint.usuario.controller;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookpoint.usuario.dto.UsuarioDTO;
import com.bookpoint.usuario.model.Usuario;
import com.bookpoint.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuario", description = "CRUD de Usuario - BookPoint Chile")
public class UsuarioController {
    @Autowired private UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Crear usuario")
    public UsuarioDTO crear(@Valid @RequestBody UsuarioDTO dto) {
        return UsuarioDTO.fromEntity(usuarioService.guardarUsuario(dto.toEntity()));
    }
    @GetMapping
    @Operation(summary = "Listar todos")
    public List<UsuarioDTO> listarTodos() {
        return usuarioService.listarUsuarios().stream().map(UsuarioDTO::fromEntity).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener por ID")
    public ResponseEntity<UsuarioDTO> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerUsuarioPorId(id).map(UsuarioDTO::fromEntity).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar")
    public ResponseEntity<UsuarioDTO> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
        try { return ResponseEntity.ok(UsuarioDTO.fromEntity(usuarioService.actualizarUsuario(id, dto.toEntity()))); }
        catch (RuntimeException ex) { return ResponseEntity.notFound().build(); }
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
