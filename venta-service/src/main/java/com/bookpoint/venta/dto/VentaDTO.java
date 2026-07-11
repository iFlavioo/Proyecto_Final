package com.bookpoint.venta.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.bookpoint.venta.model.Venta;
import com.bookpoint.venta.model.VentaDetalle;

@Data @NoArgsConstructor @AllArgsConstructor
public class VentaDTO {
    private Long id;

    @NotNull(message = "El usuarioId no puede ser nulo ni vacio")
    @Positive(message = "El usuarioId debe ser mayor a cero")
    private Long usuarioId;

    @NotNull(message = "El sucursalId no puede ser nulo ni vacio")
    @Positive(message = "El sucursalId debe ser mayor a cero")
    private Long sucursalId;

    /** Se asigna automaticamente en el servidor con la fecha actual; solo lectura para el cliente. */
    private LocalDate fechaVenta;

    /** Calculado automaticamente por el servidor a partir de los detalles */
    private Double total;

    @NotEmpty(message = "La venta debe tener al menos un producto en el detalle")
    @Valid
    private List<VentaDetalleDTO> detalles;

    public static VentaDTO fromEntity(Venta v) {
        List<VentaDetalleDTO> dtos = v.getDetalles() == null ? new ArrayList<>() :
            v.getDetalles().stream().map(VentaDetalleDTO::fromEntity).collect(Collectors.toList());
        return new VentaDTO(v.getId(), v.getUsuarioId(), v.getSucursalId(), v.getFechaVenta(), v.getTotal(), dtos);
    }

    public Venta toEntity() {
        Venta v = new Venta();
        v.setUsuarioId(usuarioId);
        v.setSucursalId(sucursalId);
        v.setFechaVenta(fechaVenta);
        if (detalles != null) {
            List<VentaDetalle> lista = detalles.stream().map(VentaDetalleDTO::toEntity).collect(Collectors.toList());
            lista.forEach(d -> d.setVenta(v));
            v.setDetalles(lista);
        }
        return v;
    }
}
