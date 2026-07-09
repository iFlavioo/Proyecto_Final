package com.bookpoint.producto.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "productos")
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Size(max = 500, message = "La descripcion no puede superar 500 caracteres")
    @Column(name = "descripcion")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    @Column(name = "precio", nullable = false)
    private Double precio;

    @NotBlank(message = "La categoria es obligatoria")
    @Column(name = "categoria", nullable = false)
    private String categoria;

    @Column(name = "autor")
    private String autor;

    @Column(name = "editorial")
    private String editorial;
}
