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

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private Double precio;

    private String categoria;
    private String autor;
    private String editorial;
}
