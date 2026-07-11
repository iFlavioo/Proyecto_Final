package com.bookpoint.inventario.controller;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookpoint.inventario.dto.InventarioDTO;
import com.bookpoint.inventario.dto.DescontarStockDTO;
import com.bookpoint.inventario.model.Inventario;
import com.bookpoint.inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/inventario")
@Tag(name = "Inventario", description = "CRUD de Inventario + descuento de stock - BookPoint Chile")
public class InventarioController {
    @Autowired private InventarioService inventarioService;

    @PostMapping
    @Operation(summary = "Registrar inventario")
    public InventarioDTO crear(@Valid @RequestBody InventarioDTO dto) {
        return InventarioDTO.fromEntity(inventarioService.guardarInventario(dto.toEntity()));
    }

    @GetMapping
    @Operation(summary = "Listar inventario")
    public List<InventarioDTO> listarTodos() {
        return inventarioService.listarInventarios().stream().map(InventarioDTO::fromEntity).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener inventario por ID")
    public ResponseEntity<InventarioDTO> obtenerPorId(@PathVariable Long id) {
        return inventarioService.obtenerInventarioPorId(id).map(InventarioDTO::fromEntity).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar inventario")
    public ResponseEntity<InventarioDTO> actualizar(@PathVariable Long id, @Valid @RequestBody InventarioDTO dto) {
        try { return ResponseEntity.ok(InventarioDTO.fromEntity(inventarioService.actualizarInventario(id, dto.toEntity()))); }
        catch (RuntimeException ex) { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar inventario")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inventarioService.eliminarInventario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/descontar")
    @Operation(summary = "Descontar stock al vender (llamado por venta-service)")
    public ResponseEntity<Void> descontarStock(@Valid @RequestBody DescontarStockDTO dto) {
        try {
            inventarioService.descontarStock(dto.getProductoId(), dto.getSucursalId(), dto.getCantidad());
            return ResponseEntity.ok().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/consultar")
    @Operation(summary = "Consultar stock de un producto en una sucursal (usado por venta-service)")
    public ResponseEntity<InventarioDTO> consultarStock(
            @RequestParam Long productoId, @RequestParam Long sucursalId) {
        return inventarioService.consultarStock(productoId, sucursalId)
            .map(InventarioDTO::fromEntity)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
