package com.bookpoint.pedido.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import com.bookpoint.pedido.model.Pedido;

@Data @NoArgsConstructor
public class PedidoDTO {
    private Long id;

    @NotNull(message = "El usuarioId es obligatorio")
    @Positive(message = "El usuarioId debe ser mayor a cero")
    private Long usuarioId;

    @NotNull(message = "El productoId es obligatorio")
    @Positive(message = "El productoId debe ser mayor a cero")
    private Long productoId;

    @Min(value = 1, message = "La cantidad minima es 1")
    private int cantidad;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "PENDIENTE|ENVIADO|ENTREGADO|CANCELADO",
             message = "Estado invalido. Use: PENDIENTE, ENVIADO, ENTREGADO o CANCELADO")
    private String estado;

    @NotNull(message = "La fecha del pedido es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaPedido;

    @NotBlank(message = "La direccion de entrega es obligatoria")
    private String direccionEntrega;

    /** Informacion enriquecida opcional — no se persiste, solo se completa al responder */
    private ProductoDTO producto;
    private UsuarioDTO usuario;
    private DespachoDTO despacho;

    public PedidoDTO(Long id, Long usuarioId, Long productoId, int cantidad,
                      String estado, LocalDate fechaPedido, String direccionEntrega) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.estado = estado;
        this.fechaPedido = fechaPedido;
        this.direccionEntrega = direccionEntrega;
    }

    public static PedidoDTO fromEntity(Pedido p) {
        return new PedidoDTO(p.getId(), p.getUsuarioId(), p.getProductoId(),
            p.getCantidad(), p.getEstado(), p.getFechaPedido(), p.getDireccionEntrega());
    }
    public Pedido toEntity() {
        return new Pedido(id, usuarioId, productoId, cantidad, estado, fechaPedido, direccionEntrega);
    }
}
