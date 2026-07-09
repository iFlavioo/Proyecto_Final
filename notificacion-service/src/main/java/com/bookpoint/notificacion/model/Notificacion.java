package com.bookpoint.notificacion.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "notificaciones")
public class Notificacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El usuarioId es obligatorio")
    @Positive(message = "El usuarioId debe ser mayor a cero")
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(min = 5, max = 500, message = "El mensaje debe tener entre 5 y 500 caracteres")
    @Column(name = "mensaje", nullable = false, length = 500)
    private String mensaje;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "leida")
    private Boolean leida;

    @NotNull(message = "La fecha de creacion es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;
}
