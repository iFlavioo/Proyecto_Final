package com.bookpoint.notificacion.controller;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookpoint.notificacion.dto.NotificacionDTO;
import com.bookpoint.notificacion.model.Notificacion;
import com.bookpoint.notificacion.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/notificaciones")
@Tag(name = "Notificacion", description = "CRUD de Notificacion - BookPoint Chile")
public class NotificacionController {
    @Autowired private NotificacionService notificacionService;

    @PostMapping
    @Operation(summary = "Crear notificacion")
    public NotificacionDTO crear(@Valid @RequestBody NotificacionDTO dto) {
        return NotificacionDTO.fromEntity(notificacionService.guardarNotificacion(dto.toEntity()));
    }
    @GetMapping
    @Operation(summary = "Listar todos")
    public List<NotificacionDTO> listarTodos() {
        return notificacionService.listarNotificacions().stream().map(NotificacionDTO::fromEntity).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener por ID")
    public ResponseEntity<NotificacionDTO> obtenerPorId(@PathVariable Long id) {
        return notificacionService.obtenerNotificacionPorId(id).map(NotificacionDTO::fromEntity).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar")
    public ResponseEntity<NotificacionDTO> actualizar(@PathVariable Long id, @Valid @RequestBody NotificacionDTO dto) {
        try { return ResponseEntity.ok(NotificacionDTO.fromEntity(notificacionService.actualizarNotificacion(id, dto.toEntity()))); }
        catch (RuntimeException ex) { return ResponseEntity.notFound().build(); }
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        notificacionService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }
}
