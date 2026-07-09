package com.bookpoint.transferencia.controller;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookpoint.transferencia.dto.TransferenciaDTO;
import com.bookpoint.transferencia.model.Transferencia;
import com.bookpoint.transferencia.service.TransferenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/transferencias")
@Tag(name = "Transferencia", description = "CRUD de Transferencia - BookPoint Chile")
public class TransferenciaController {
    @Autowired private TransferenciaService transferenciaService;

    @PostMapping
    @Operation(summary = "Crear transferencia")
    public TransferenciaDTO crear(@Valid @RequestBody TransferenciaDTO dto) {
        return TransferenciaDTO.fromEntity(transferenciaService.guardarTransferencia(dto.toEntity()));
    }
    @GetMapping
    @Operation(summary = "Listar todos")
    public List<TransferenciaDTO> listarTodos() {
        return transferenciaService.listarTransferencias().stream().map(TransferenciaDTO::fromEntity).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener por ID")
    public ResponseEntity<TransferenciaDTO> obtenerPorId(@PathVariable Long id) {
        return transferenciaService.obtenerTransferenciaPorId(id).map(TransferenciaDTO::fromEntity).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar")
    public ResponseEntity<TransferenciaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody TransferenciaDTO dto) {
        try { return ResponseEntity.ok(TransferenciaDTO.fromEntity(transferenciaService.actualizarTransferencia(id, dto.toEntity()))); }
        catch (RuntimeException ex) { return ResponseEntity.notFound().build(); }
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        transferenciaService.eliminarTransferencia(id);
        return ResponseEntity.noContent().build();
    }
}
