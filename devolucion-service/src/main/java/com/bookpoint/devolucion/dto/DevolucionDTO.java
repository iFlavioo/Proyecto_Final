package com.bookpoint.devolucion.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import com.bookpoint.devolucion.model.Devolucion;

@Data @NoArgsConstructor @AllArgsConstructor
public class DevolucionDTO {
    private Long id;

    @NotNull(message = "El usuarioId es obligatorio")
    @Positive(message = "El usuarioId debe ser mayor a cero")
    private Long usuarioId;

    @NotNull(message = "El productoId es obligatorio")
    @Positive(message = "El productoId debe ser mayor a cero")
    private Long productoId;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(min = 5, max = 300, message = "El motivo debe tener entre 5 y 300 caracteres")
    private String motivo;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "PENDIENTE|APROBADA|RECHAZADA",
             message = "Estado invalido. Use: PENDIENTE, APROBADA o RECHAZADA")
    private String estado;

    @NotNull(message = "La fecha de devolucion es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaDevolucion;

    public static DevolucionDTO fromEntity(Devolucion d) {
        return new DevolucionDTO(d.getId(), d.getUsuarioId(), d.getProductoId(),
            d.getMotivo(), d.getEstado(), d.getFechaDevolucion());
    }
    public Devolucion toEntity() {
        return new Devolucion(id, usuarioId, productoId, motivo, estado, fechaDevolucion);
    }
}
