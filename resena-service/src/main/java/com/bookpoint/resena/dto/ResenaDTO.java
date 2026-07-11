package com.bookpoint.resena.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import com.bookpoint.resena.model.Resena;

@Data @NoArgsConstructor @AllArgsConstructor
public class ResenaDTO {
    private Long id;

    @NotNull(message = "El usuarioId no puede ser nulo ni vacio")
    @Positive(message = "El usuarioId debe ser mayor a cero")
    private Long usuarioId;

    @NotNull(message = "El productoId no puede ser nulo ni vacio")
    @Positive(message = "El productoId debe ser mayor a cero")
    private Long productoId;

    @NotBlank(message = "El comentario no puede ser nulo ni vacio")
    @Size(min = 5, max = 500, message = "El comentario debe tener entre 5 y 500 caracteres")
    private String comentario;

    @Min(value = 1, message = "La calificacion minima es 1")
    @Max(value = 5, message = "La calificacion maxima es 5")
    private int calificacion;

    @NotNull(message = "La fecha de resena no puede ser nula ni vacia")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaResena;

    public static ResenaDTO fromEntity(Resena r) {
        return new ResenaDTO(r.getId(), r.getUsuarioId(), r.getProductoId(),
            r.getComentario(), r.getCalificacion(), r.getFechaResena());
    }
    public Resena toEntity() {
        return new Resena(id, usuarioId, productoId, comentario, calificacion, fechaResena);
    }
}
