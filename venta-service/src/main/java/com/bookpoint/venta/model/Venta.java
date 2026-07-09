package com.bookpoint.venta.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "ventas")
public class Venta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "sucursal_id", nullable = false)
    private Long sucursalId;

    @Column(name = "fecha_venta")
    private LocalDate fechaVenta;

    @Column(name = "total")
    private Double total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<VentaDetalle> detalles = new ArrayList<>();
}
