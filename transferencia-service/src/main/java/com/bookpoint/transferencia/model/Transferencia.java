package com.bookpoint.transferencia.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "transferencias")
public class Transferencia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El productoId es obligatorio")
    @Positive(message = "El productoId debe ser mayor a cero")
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @NotNull(message = "La sucursal de origen es obligatoria")
    @Positive(message = "El sucursalOrigenId debe ser mayor a cero")
    @Column(name = "sucursal_origen_id", nullable = false)
    private Long sucursalOrigenId;

    @NotNull(message = "La sucursal de destino es obligatoria")
    @Positive(message = "El sucursalDestinoId debe ser mayor a cero")
    @Column(name = "sucursal_destino_id", nullable = false)
    private Long sucursalDestinoId;

    @Min(value = 1, message = "La cantidad minima a transferir es 1")
    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Pattern(regexp = "PENDIENTE|APROBADA|RECHAZADA",
             message = "Estado invalido. Use: PENDIENTE, APROBADA o RECHAZADA")
    @Column(name = "estado")
    private String estado;

    @NotNull(message = "La fecha de transferencia es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    @Column(name = "fecha_transferencia")
    private LocalDate fechaTransferencia;
}
