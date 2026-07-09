package com.bookpoint.sucursal.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "sucursales")
public class Sucursal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotBlank(message = "La direccion es obligatoria")
    @Column(name = "direccion", nullable = false)
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    @Pattern(regexp = "Concepcion|Temuco|La Serena",
             message = "Ciudad invalida. Use: Concepcion, Temuco o La Serena")
    @Column(name = "ciudad", nullable = false)
    private String ciudad;

    @Pattern(regexp = "\\d{9}", message = "El telefono debe tener 9 digitos")
    @Column(name = "telefono")
    private String telefono;

    @Column(name = "horario")
    private String horario;
}
