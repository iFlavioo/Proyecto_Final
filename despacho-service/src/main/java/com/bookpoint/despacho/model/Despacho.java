package com.bookpoint.despacho.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "despachos")
public class Despacho {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El pedidoId es obligatorio")
    @Positive(message = "El pedidoId debe ser mayor a cero")
    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    @NotNull(message = "El sucursalId es obligatorio")
    @Positive(message = "El sucursalId debe ser mayor a cero")
    @Column(name = "sucursal_id", nullable = false)
    private Long sucursalId;

    @Pattern(regexp = "PREPARANDO|EN_CAMINO|ENTREGADO",
             message = "Estado invalido. Use: PREPARANDO, EN_CAMINO o ENTREGADO")
    @Column(name = "estado")
    private String estado;

    @NotNull(message = "La fecha de despacho es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    @Column(name = "fecha_despacho")
    private LocalDate fechaDespacho;

    @NotBlank(message = "La direccion de destino es obligatoria")
    @Column(name = "direccion_destino")
    private String direccionDestino;
}
