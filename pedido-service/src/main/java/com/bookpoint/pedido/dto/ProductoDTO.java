package com.bookpoint.pedido.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la informacion de un Producto dentro del contexto de pedido-service.
 * Se usa para enriquecer la respuesta de un pedido con los datos del producto solicitado,
 * sin necesidad de exponer directamente la entidad de producto-service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private Long id;

    @NotBlank(message = "El nombre del producto no puede ser nulo ni vacio")
    private String nombre;

    @NotBlank(message = "La descripcion no puede ser nula ni vacia")
    private String descripcion;

    @NotNull(message = "El precio no puede ser nulo ni vacio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private Double precio;

    @NotBlank(message = "La categoria no puede ser nula ni vacia")
    private String categoria;

    @NotBlank(message = "El autor no puede ser nulo ni vacio")
    private String autor;

    @NotBlank(message = "La editorial no puede ser nula ni vacia")
    private String editorial;
}
