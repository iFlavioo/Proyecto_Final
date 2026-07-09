package com.bookpoint.venta.dto;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.bookpoint.venta.model.Venta;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas directas de VentaDTO para cubrir las ramas donde "detalles"
 * puede ser null (no cubiertas por los flujos normales de Controller/Service,
 * ya que en la practica siempre se envia al menos un detalle).
 */
class VentaDTOTest {

    @Test
    void fromEntityConDetallesNullRetornaListaVacia() {
        Venta venta = new Venta();
        venta.setId(1L);
        venta.setUsuarioId(1L);
        venta.setSucursalId(1L);
        venta.setFechaVenta(LocalDate.now());
        venta.setTotal(0.0);
        venta.setDetalles(null);

        VentaDTO dto = VentaDTO.fromEntity(venta);

        assertThat(dto.getDetalles()).isNotNull();
        assertThat(dto.getDetalles()).isEmpty();
    }

    @Test
    void fromEntityConDetallesRetornaListaMapeada() {
        Venta venta = new Venta();
        venta.setId(1L);
        venta.setDetalles(new java.util.ArrayList<>());

        VentaDTO dto = VentaDTO.fromEntity(venta);

        assertThat(dto.getDetalles()).isNotNull();
    }

    @Test
    void toEntityConDetallesNullNoFalla() {
        VentaDTO dto = new VentaDTO(null, 1L, 1L, LocalDate.now(), null, null);

        Venta venta = dto.toEntity();

        assertThat(venta.getUsuarioId()).isEqualTo(1L);
        assertThat(venta.getSucursalId()).isEqualTo(1L);
    }

    @Test
    void toEntityConDetallesAsignaVentaACadaUno() {
        VentaDetalleDTO detalle = new VentaDetalleDTO(null, 1L, 2, null, null);
        VentaDTO dto = new VentaDTO(null, 1L, 1L, LocalDate.now(), null, java.util.List.of(detalle));

        Venta venta = dto.toEntity();

        assertThat(venta.getDetalles()).hasSize(1);
        assertThat(venta.getDetalles().get(0).getVenta()).isEqualTo(venta);
    }
}
