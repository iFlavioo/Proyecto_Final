package com.bookpoint.venta.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import com.bookpoint.venta.model.VentaDetalle;

@Data @NoArgsConstructor @AllArgsConstructor
public class VentaDetalleDTO {
    private Long id;

    @NotNull(message = "El productoId no puede ser nulo ni vacio")
    @Positive(message = "El productoId debe ser mayor a cero")
    private Long productoId;

    @NotNull(message = "La cantidad no puede ser nula ni vacia")
    @Min(value = 1, message = "La cantidad minima es 1")
    private Integer cantidad;

    /** Estos dos campos son calculados por el servidor (no se toman del cliente) */
    private Double precioUnitario;
    private Double subtotal;

    public static VentaDetalleDTO fromEntity(VentaDetalle d) {
        return new VentaDetalleDTO(d.getId(), d.getProductoId(), d.getCantidad(), d.getPrecioUnitario(), d.getSubtotal());
    }

    /** Convierte a entidad SIN precio — el precio real se completa en VentaService consultando producto-service */
    public VentaDetalle toEntity() {
        VentaDetalle d = new VentaDetalle();
        d.setProductoId(productoId);
        d.setCantidad(cantidad);
        return d;
    }
}
