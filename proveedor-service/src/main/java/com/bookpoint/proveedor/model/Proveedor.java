package com.bookpoint.proveedor.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "proveedores")
public class Proveedor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del proveedor es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Email(message = "El email no tiene un formato valido")
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "editorial")
    private String editorial;

    @Column(name = "activo")
    private Boolean activo;
}
