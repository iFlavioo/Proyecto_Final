package com.bookpoint.despacho.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import com.bookpoint.despacho.model.Despacho;

@Data @NoArgsConstructor @AllArgsConstructor
public class DespachoDTO {
    private Long id;

    @NotNull(message = "El pedidoId no puede ser nulo ni vacio")
    @Positive(message = "El pedidoId debe ser mayor a cero")
    private Long pedidoId;

    @NotNull(message = "El sucursalId no puede ser nulo ni vacio")
    @Positive(message = "El sucursalId debe ser mayor a cero")
    private Long sucursalId;

    @NotBlank(message = "El estado no puede ser nulo ni vacio")
    @Pattern(regexp = "PREPARANDO|EN_CAMINO|ENTREGADO",
             message = "Estado invalido. Use: PREPARANDO, EN_CAMINO o ENTREGADO")
    private String estado;

    @NotNull(message = "La fecha de despacho no puede ser nula ni vacia")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaDespacho;

    @NotBlank(message = "La direccion de destino no puede ser nula ni vacia")
    private String direccionDestino;

    public static DespachoDTO fromEntity(Despacho d) {
        return new DespachoDTO(d.getId(), d.getPedidoId(), d.getSucursalId(),
            d.getEstado(), d.getFechaDespacho(), d.getDireccionDestino());
    }
    public Despacho toEntity() {
        return new Despacho(id, pedidoId, sucursalId, estado, fechaDespacho, direccionDestino);
    }
}
