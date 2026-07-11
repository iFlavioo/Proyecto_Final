package com.bookpoint.descuento.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import com.bookpoint.descuento.model.Descuento;

@Data @NoArgsConstructor @AllArgsConstructor
public class DescuentoDTO {
    private Long id;

    @NotBlank(message = "El codigo no puede ser nulo ni vacio")
    @Size(min = 3, max = 50, message = "El codigo debe tener entre 3 y 50 caracteres")
    private String codigo;

    @NotBlank(message = "La descripcion no puede ser nula ni vacia")
    private String descripcion;

    @NotNull(message = "El porcentaje no puede ser nulo ni vacio")
    @DecimalMin(value = "0.1", message = "El porcentaje debe ser mayor a 0")
    @DecimalMax(value = "100.0", message = "El porcentaje no puede superar 100")
    private Double porcentaje;

    @NotBlank(message = "El tipo no puede ser nulo ni vacio")
    @Pattern(regexp = "CUPON|CONVENIO_ESTUDIANTIL|PROMOCION",
             message = "Tipo invalido. Use: CUPON, CONVENIO_ESTUDIANTIL o PROMOCION")
    private String tipo;

    @NotNull(message = "El campo activo no puede ser nulo")
    private Boolean activo;

    @NotNull(message = "La fecha de expiracion no puede ser nula ni vacia")
    @Future(message = "La fecha de expiracion debe ser una fecha futura")
    private LocalDate fechaExpiracion;

    public static DescuentoDTO fromEntity(Descuento d) {
        return new DescuentoDTO(d.getId(), d.getCodigo(), d.getDescripcion(),
            d.getPorcentaje(), d.getTipo(), d.getActivo(), d.getFechaExpiracion());
    }
    public Descuento toEntity() {
        return new Descuento(id, codigo, descripcion, porcentaje, tipo, activo, fechaExpiracion);
    }
}
