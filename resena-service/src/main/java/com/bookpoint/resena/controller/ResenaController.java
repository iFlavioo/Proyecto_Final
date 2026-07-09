package com.bookpoint.resena.controller;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookpoint.resena.dto.ResenaDTO;
import com.bookpoint.resena.model.Resena;
import com.bookpoint.resena.service.ResenaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/resenas")
@Tag(name = "Resena", description = "CRUD de Resena - BookPoint Chile")
public class ResenaController {
    @Autowired private ResenaService resenaService;

    @PostMapping
    @Operation(summary = "Crear resena")
    public ResenaDTO crear(@Valid @RequestBody ResenaDTO dto) {
        return ResenaDTO.fromEntity(resenaService.guardarResena(dto.toEntity()));
    }
    @GetMapping
    @Operation(summary = "Listar todos")
    public List<ResenaDTO> listarTodos() {
        return resenaService.listarResenas().stream().map(ResenaDTO::fromEntity).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener por ID")
    public ResponseEntity<ResenaDTO> obtenerPorId(@PathVariable Long id) {
        return resenaService.obtenerResenaPorId(id).map(ResenaDTO::fromEntity).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar")
    public ResponseEntity<ResenaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ResenaDTO dto) {
        try { return ResponseEntity.ok(ResenaDTO.fromEntity(resenaService.actualizarResena(id, dto.toEntity()))); }
        catch (RuntimeException ex) { return ResponseEntity.notFound().build(); }
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        resenaService.eliminarResena(id);
        return ResponseEntity.noContent().build();
    }
}
