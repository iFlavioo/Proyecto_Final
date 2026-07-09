package com.bookpoint.venta.controller;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookpoint.venta.dto.VentaDTO;
import com.bookpoint.venta.model.Venta;
import com.bookpoint.venta.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/v1/ventas")
@Tag(name = "Ventas", description = "CRUD de Ventas con multiples productos - BookPoint Chile")
public class VentaController {
    @Autowired private VentaService ventaService;

    @PostMapping
    @Operation(summary = "Crear venta con uno o multiples productos (precio y total calculados automaticamente)")
    public VentaDTO crear(@Valid @RequestBody VentaDTO dto) {
        return VentaDTO.fromEntity(ventaService.guardarVenta(dto.toEntity()));
    }
    @GetMapping
    @Operation(summary = "Listar todas las ventas")
    public List<VentaDTO> listarTodas() {
        return ventaService.listarVentas().stream().map(VentaDTO::fromEntity).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener venta por ID")
    public ResponseEntity<VentaDTO> obtenerPorId(@PathVariable Long id) {
        return ventaService.obtenerVentaPorId(id).map(VentaDTO::fromEntity).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar venta")
    public ResponseEntity<VentaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody VentaDTO dto) {
        try { return ResponseEntity.ok(VentaDTO.fromEntity(ventaService.actualizarVenta(id, dto.toEntity()))); }
        catch (RuntimeException ex) { return ResponseEntity.notFound().build(); }
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar venta")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ventaService.eliminarVenta(id);
        return ResponseEntity.noContent().build();
    }
}
