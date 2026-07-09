package com.bookpoint.devolucion.controller;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookpoint.devolucion.dto.DevolucionDTO;
import com.bookpoint.devolucion.model.Devolucion;
import com.bookpoint.devolucion.service.DevolucionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/devoluciones")
@Tag(name = "Devolucion", description = "CRUD de Devolucion - BookPoint Chile")
public class DevolucionController {
    @Autowired private DevolucionService devolucionService;

    @PostMapping
    @Operation(summary = "Crear devolucion")
    public DevolucionDTO crear(@Valid @RequestBody DevolucionDTO dto) {
        return DevolucionDTO.fromEntity(devolucionService.guardarDevolucion(dto.toEntity()));
    }
    @GetMapping
    @Operation(summary = "Listar todos")
    public List<DevolucionDTO> listarTodos() {
        return devolucionService.listarDevolucions().stream().map(DevolucionDTO::fromEntity).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener por ID")
    public ResponseEntity<DevolucionDTO> obtenerPorId(@PathVariable Long id) {
        return devolucionService.obtenerDevolucionPorId(id).map(DevolucionDTO::fromEntity).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar")
    public ResponseEntity<DevolucionDTO> actualizar(@PathVariable Long id, @Valid @RequestBody DevolucionDTO dto) {
        try { return ResponseEntity.ok(DevolucionDTO.fromEntity(devolucionService.actualizarDevolucion(id, dto.toEntity()))); }
        catch (RuntimeException ex) { return ResponseEntity.notFound().build(); }
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        devolucionService.eliminarDevolucion(id);
        return ResponseEntity.noContent().build();
    }
}
