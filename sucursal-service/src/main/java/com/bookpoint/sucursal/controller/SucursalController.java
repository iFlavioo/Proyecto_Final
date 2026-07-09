package com.bookpoint.sucursal.controller;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookpoint.sucursal.dto.SucursalDTO;
import com.bookpoint.sucursal.model.Sucursal;
import com.bookpoint.sucursal.service.SucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/sucursales")
@Tag(name = "Sucursal", description = "CRUD de Sucursal - BookPoint Chile")
public class SucursalController {
    @Autowired private SucursalService sucursalService;

    @PostMapping
    @Operation(summary = "Crear sucursal")
    public SucursalDTO crear(@Valid @RequestBody SucursalDTO dto) {
        return SucursalDTO.fromEntity(sucursalService.guardarSucursal(dto.toEntity()));
    }
    @GetMapping
    @Operation(summary = "Listar todos")
    public List<SucursalDTO> listarTodos() {
        return sucursalService.listarSucursals().stream().map(SucursalDTO::fromEntity).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener por ID")
    public ResponseEntity<SucursalDTO> obtenerPorId(@PathVariable Long id) {
        return sucursalService.obtenerSucursalPorId(id).map(SucursalDTO::fromEntity).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar")
    public ResponseEntity<SucursalDTO> actualizar(@PathVariable Long id, @Valid @RequestBody SucursalDTO dto) {
        try { return ResponseEntity.ok(SucursalDTO.fromEntity(sucursalService.actualizarSucursal(id, dto.toEntity()))); }
        catch (RuntimeException ex) { return ResponseEntity.notFound().build(); }
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        sucursalService.eliminarSucursal(id);
        return ResponseEntity.noContent().build();
    }
}
