package com.bookpoint.despacho.controller;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookpoint.despacho.dto.DespachoDTO;
import com.bookpoint.despacho.model.Despacho;
import com.bookpoint.despacho.service.DespachoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/despachos")
@Tag(name = "Despacho", description = "CRUD de Despacho - BookPoint Chile")
public class DespachoController {
    @Autowired private DespachoService despachoService;

    @PostMapping
    @Operation(summary = "Crear despacho")
    public DespachoDTO crear(@Valid @RequestBody DespachoDTO dto) {
        return DespachoDTO.fromEntity(despachoService.guardarDespacho(dto.toEntity()));
    }
    @GetMapping
    @Operation(summary = "Listar todos")
    public List<DespachoDTO> listarTodos() {
        return despachoService.listarDespachos().stream().map(DespachoDTO::fromEntity).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener por ID")
    public ResponseEntity<DespachoDTO> obtenerPorId(@PathVariable Long id) {
        return despachoService.obtenerDespachoPorId(id).map(DespachoDTO::fromEntity).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar")
    public ResponseEntity<DespachoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody DespachoDTO dto) {
        try { return ResponseEntity.ok(DespachoDTO.fromEntity(despachoService.actualizarDespacho(id, dto.toEntity()))); }
        catch (RuntimeException ex) { return ResponseEntity.notFound().build(); }
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        despachoService.eliminarDespacho(id);
        return ResponseEntity.noContent().build();
    }
}
