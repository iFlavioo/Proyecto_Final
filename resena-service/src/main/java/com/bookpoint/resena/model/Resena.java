package com.bookpoint.resena.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "resenas")
public class Resena {
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

    @NotBlank(message = "El comentario es obligatorio")
    @Size(min = 5, max = 500, message = "El comentario debe tener entre 5 y 500 caracteres")
    @Column(name = "comentario", nullable = false, length = 500)
    private String comentario;

    @Min(value = 1, message = "La calificacion minima es 1")
    @Max(value = 5, message = "La calificacion maxima es 5")
    @Column(name = "calificacion", nullable = false)
    private int calificacion;

    @NotNull(message = "La fecha de resena es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    @Column(name = "fecha_resena")
    private LocalDate fechaResena;
}
