package com.bookpoint.devolucion.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "devoluciones")
public class Devolucion {
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

    @NotBlank(message = "El motivo es obligatorio")
    @Size(min = 5, max = 300, message = "El motivo debe tener entre 5 y 300 caracteres")
    @Column(name = "motivo", nullable = false, length = 300)
    private String motivo;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "PENDIENTE|APROBADA|RECHAZADA",
             message = "Estado invalido. Use: PENDIENTE, APROBADA o RECHAZADA")
    @Column(name = "estado", nullable = false)
    private String estado;

    @NotNull(message = "La fecha de devolucion es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;
}
