package com.bookpoint.notificacion.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import com.bookpoint.notificacion.model.Notificacion;

@Data @NoArgsConstructor @AllArgsConstructor
public class NotificacionDTO {
    private Long id;

    @NotNull(message = "El usuarioId no puede ser nulo ni vacio")
    @Positive(message = "El usuarioId debe ser mayor a cero")
    private Long usuarioId;

    @NotBlank(message = "El mensaje no puede ser nulo ni vacio")
    @Size(min = 5, max = 500, message = "El mensaje debe tener entre 5 y 500 caracteres")
    private String mensaje;

    @NotBlank(message = "El tipo no puede ser nulo ni vacio")
    private String tipo;

    @NotNull(message = "El campo leida no puede ser nulo")
    private Boolean leida;

    @NotNull(message = "La fecha de creacion no puede ser nula ni vacia")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaCreacion;

    public static NotificacionDTO fromEntity(Notificacion n) {
        return new NotificacionDTO(n.getId(), n.getUsuarioId(), n.getMensaje(),
            n.getTipo(), n.getLeida(), n.getFechaCreacion());
    }
    public Notificacion toEntity() {
        return new Notificacion(id, usuarioId, mensaje, tipo, leida, fechaCreacion);
    }
}
