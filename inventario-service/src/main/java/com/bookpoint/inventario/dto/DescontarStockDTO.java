package com.bookpoint.inventario.dto;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class DescontarStockDTO {
    private Long productoId;
    private Long sucursalId;
    private Integer cantidad;
}
