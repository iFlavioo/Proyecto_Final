package com.bookpoint.pedido.controller;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookpoint.pedido.dto.PedidoDTO;
import com.bookpoint.pedido.model.Pedido;
import com.bookpoint.pedido.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedido", description = "CRUD de Pedido - BookPoint Chile")
public class PedidoController {
    @Autowired private PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Crear pedido")
    public PedidoDTO crear(@Valid @RequestBody PedidoDTO dto) {
        return PedidoDTO.fromEntity(pedidoService.guardarPedido(dto.toEntity()));
    }
    @GetMapping
    @Operation(summary = "Listar todos")
    public List<PedidoDTO> listarTodos() {
        return pedidoService.listarPedidos().stream().map(PedidoDTO::fromEntity).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener por ID")
    public ResponseEntity<PedidoDTO> obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPedidoPorId(id).map(PedidoDTO::fromEntity).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar")
    public ResponseEntity<PedidoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody PedidoDTO dto) {
        try { return ResponseEntity.ok(PedidoDTO.fromEntity(pedidoService.actualizarPedido(id, dto.toEntity()))); }
        catch (RuntimeException ex) { return ResponseEntity.notFound().build(); }
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
