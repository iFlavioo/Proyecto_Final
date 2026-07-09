package com.bookpoint.venta.dto;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class DescontarStockRequestDTO {
    private Long productoId;
    private Long sucursalId;
    private Integer cantidad;
}
