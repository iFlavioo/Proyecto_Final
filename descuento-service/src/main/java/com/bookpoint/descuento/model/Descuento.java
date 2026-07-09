package com.bookpoint.descuento.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "descuentos")
public class Descuento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El codigo es obligatorio")
    @Size(min = 3, max = 50, message = "El codigo debe tener entre 3 y 50 caracteres")
    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @Column(name = "descripcion")
    private String descripcion;

    @NotNull(message = "El porcentaje es obligatorio")
    @DecimalMin(value = "0.1", message = "El porcentaje debe ser mayor a 0")
    @DecimalMax(value = "100.0", message = "El porcentaje no puede superar 100")
    @Column(name = "porcentaje", nullable = false)
    private Double porcentaje;

    @Pattern(regexp = "CUPON|CONVENIO_ESTUDIANTIL|PROMOCION",
             message = "Tipo invalido. Use: CUPON, CONVENIO_ESTUDIANTIL o PROMOCION")
    @Column(name = "tipo")
    private String tipo;

    @Column(name = "activo")
    private Boolean activo;

    @Future(message = "La fecha de expiracion debe ser una fecha futura")
    @Column(name = "fecha_expiracion")
    private LocalDate fechaExpiracion;
}
