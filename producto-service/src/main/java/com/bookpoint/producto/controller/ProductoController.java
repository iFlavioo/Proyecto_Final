package com.bookpoint.producto.controller;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookpoint.producto.dto.ProductoDTO;
import com.bookpoint.producto.model.Producto;
import com.bookpoint.producto.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Producto", description = "CRUD de Producto - BookPoint Chile")
public class ProductoController {
    @Autowired private ProductoService productoService;

    @PostMapping
    @Operation(summary = "Crear producto")
    public ProductoDTO crear(@Valid @RequestBody ProductoDTO dto) {
        return ProductoDTO.fromEntity(productoService.guardarProducto(dto.toEntity()));
    }
    @GetMapping
    @Operation(summary = "Listar todos")
    public List<ProductoDTO> listarTodos() {
        return productoService.listarProductos().stream().map(ProductoDTO::fromEntity).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener por ID")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id).map(ProductoDTO::fromEntity).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoDTO dto) {
        try { return ResponseEntity.ok(ProductoDTO.fromEntity(productoService.actualizarProducto(id, dto.toEntity()))); }
        catch (RuntimeException ex) { return ResponseEntity.notFound().build(); }
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
