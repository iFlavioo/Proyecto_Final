package com.bookpoint.pedido.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "pedidos")
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El usuarioId es obligatorio")
    @Positive(message = "El usuarioId debe ser mayor a cero")
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @NotNull(message = "El productoId es obligatorio")
    @Positive(message = "El productoId debe ser mayor a cero")
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Min(value = 1, message = "La cantidad minima es 1")
    @Column(name = "cantidad")
    private int cantidad;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "PENDIENTE|ENVIADO|ENTREGADO|CANCELADO",
             message = "Estado invalido. Use: PENDIENTE, ENVIADO, ENTREGADO o CANCELADO")
    @Column(name = "estado")
    private String estado;

    @NotNull(message = "La fecha del pedido es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    @Column(name = "fecha_pedido")
    private LocalDate fechaPedido;

    @NotBlank(message = "La direccion de entrega es obligatoria")
    @Column(name = "direccion_entrega")
    private String direccionEntrega;
}
