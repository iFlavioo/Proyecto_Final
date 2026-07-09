package com.bookpoint.inventario.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "inventario")
public class Inventario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El productoId es obligatorio")
    @Positive(message = "El productoId debe ser mayor a cero")
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @NotNull(message = "El sucursalId es obligatorio")
    @Positive(message = "El sucursalId debe ser mayor a cero")
    @Column(name = "sucursal_id", nullable = false)
    private Long sucursalId;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(name = "stock")
    private int stock;

    @Min(value = 0, message = "El stock minimo no puede ser negativo")
    @Column(name = "stock_minimo")
    private int stockMinimo;
}
