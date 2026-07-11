package com.bookpoint.venta.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class DescontarStockRequestDTO {
    @NotNull(message = "El productoId no puede ser nulo ni vacio")
    @Positive(message = "El productoId debe ser mayor a cero")
    private Long productoId;

    @NotNull(message = "El sucursalId no puede ser nulo ni vacio")
    @Positive(message = "El sucursalId debe ser mayor a cero")
    private Long sucursalId;

    @NotNull(message = "La cantidad no puede ser nula ni vacia")
    @Min(value = 1, message = "La cantidad minima es 1")
    private Integer cantidad;
}
