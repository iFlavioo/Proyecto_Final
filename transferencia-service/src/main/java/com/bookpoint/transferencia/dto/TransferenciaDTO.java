package com.bookpoint.transferencia.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import com.bookpoint.transferencia.model.Transferencia;

@Data @NoArgsConstructor @AllArgsConstructor
public class TransferenciaDTO {
    private Long id;

    @NotNull(message = "El productoId es obligatorio")
    @Positive(message = "El productoId debe ser mayor a cero")
    private Long productoId;

    @NotNull(message = "La sucursal de origen es obligatoria")
    @Positive(message = "El sucursalOrigenId debe ser mayor a cero")
    private Long sucursalOrigenId;

    @NotNull(message = "La sucursal de destino es obligatoria")
    @Positive(message = "El sucursalDestinoId debe ser mayor a cero")
    private Long sucursalDestinoId;

    @Min(value = 1, message = "La cantidad minima a transferir es 1")
    private int cantidad;

    @Pattern(regexp = "PENDIENTE|APROBADA|RECHAZADA",
             message = "Estado invalido. Use: PENDIENTE, APROBADA o RECHAZADA")
    private String estado;

    @NotNull(message = "La fecha de transferencia es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaTransferencia;

    public static TransferenciaDTO fromEntity(Transferencia t) {
        return new TransferenciaDTO(t.getId(), t.getProductoId(), t.getSucursalOrigenId(),
            t.getSucursalDestinoId(), t.getCantidad(), t.getEstado(), t.getFechaTransferencia());
    }
    public Transferencia toEntity() {
        return new Transferencia(id, productoId, sucursalOrigenId, sucursalDestinoId,
            cantidad, estado, fechaTransferencia);
    }
}
