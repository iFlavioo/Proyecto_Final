package com.bookpoint.proveedor.controller;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookpoint.proveedor.dto.ProveedorDTO;
import com.bookpoint.proveedor.model.Proveedor;
import com.bookpoint.proveedor.service.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/proveedores")
@Tag(name = "Proveedor", description = "CRUD de Proveedor - BookPoint Chile")
public class ProveedorController {
    @Autowired private ProveedorService proveedorService;

    @PostMapping
    @Operation(summary = "Crear proveedor")
    public ProveedorDTO crear(@Valid @RequestBody ProveedorDTO dto) {
        return ProveedorDTO.fromEntity(proveedorService.guardarProveedor(dto.toEntity()));
    }
    @GetMapping
    @Operation(summary = "Listar todos")
    public List<ProveedorDTO> listarTodos() {
        return proveedorService.listarProveedors().stream().map(ProveedorDTO::fromEntity).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener por ID")
    public ResponseEntity<ProveedorDTO> obtenerPorId(@PathVariable Long id) {
        return proveedorService.obtenerProveedorPorId(id).map(ProveedorDTO::fromEntity).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar")
    public ResponseEntity<ProveedorDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ProveedorDTO dto) {
        try { return ResponseEntity.ok(ProveedorDTO.fromEntity(proveedorService.actualizarProveedor(id, dto.toEntity()))); }
        catch (RuntimeException ex) { return ResponseEntity.notFound().build(); }
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        proveedorService.eliminarProveedor(id);
        return ResponseEntity.noContent().build();
    }
}
