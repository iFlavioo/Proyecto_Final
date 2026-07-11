package com.bookpoint.inventario.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import com.bookpoint.inventario.model.Inventario;

@Data @NoArgsConstructor @AllArgsConstructor
public class InventarioDTO {
    private Long id;

    @NotNull(message = "El productoId no puede ser nulo ni vacio")
    @Positive(message = "El productoId debe ser mayor a cero")
    private Long productoId;

    @NotNull(message = "El sucursalId no puede ser nulo ni vacio")
    @Positive(message = "El sucursalId debe ser mayor a cero")
    private Long sucursalId;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock;

    @Min(value = 0, message = "El stock minimo no puede ser negativo")
    private int stockMinimo;

    public static InventarioDTO fromEntity(Inventario i) {
        return new InventarioDTO(i.getId(), i.getProductoId(), i.getSucursalId(),
            i.getStock(), i.getStockMinimo());
    }
    public Inventario toEntity() {
        return new Inventario(id, productoId, sucursalId, stock, stockMinimo);
    }
}
