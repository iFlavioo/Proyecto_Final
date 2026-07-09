package com.bookpoint.descuento.controller;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookpoint.descuento.dto.DescuentoDTO;
import com.bookpoint.descuento.model.Descuento;
import com.bookpoint.descuento.service.DescuentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/descuentos")
@Tag(name = "Descuento", description = "CRUD de Descuento - BookPoint Chile")
public class DescuentoController {
    @Autowired private DescuentoService descuentoService;

    @PostMapping
    @Operation(summary = "Crear descuento")
    public DescuentoDTO crear(@Valid @RequestBody DescuentoDTO dto) {
        return DescuentoDTO.fromEntity(descuentoService.guardarDescuento(dto.toEntity()));
    }
    @GetMapping
    @Operation(summary = "Listar todos")
    public List<DescuentoDTO> listarTodos() {
        return descuentoService.listarDescuentos().stream().map(DescuentoDTO::fromEntity).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener por ID")
    public ResponseEntity<DescuentoDTO> obtenerPorId(@PathVariable Long id) {
        return descuentoService.obtenerDescuentoPorId(id).map(DescuentoDTO::fromEntity).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar")
    public ResponseEntity<DescuentoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody DescuentoDTO dto) {
        try { return ResponseEntity.ok(DescuentoDTO.fromEntity(descuentoService.actualizarDescuento(id, dto.toEntity()))); }
        catch (RuntimeException ex) { return ResponseEntity.notFound().build(); }
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        descuentoService.eliminarDescuento(id);
        return ResponseEntity.noContent().build();
    }
}
