package com.bookpoint.descuento.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bookpoint.descuento.model.Descuento;
import com.bookpoint.descuento.repository.DescuentoRepository;

public class DescuentoServiceTest {
    @Mock private DescuentoRepository descuentoRepository;
    @InjectMocks private DescuentoService descuentoService;

    @BeforeEach void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void testGuardarDescuento() {
        Descuento nuevo = new Descuento(null, "CUPON01", "Descuento verano", 10.0, "CUPON", true, java.time.LocalDate.now().plusDays(30));
        Descuento guardado = new Descuento(1L, "CUPON01", "Descuento verano", 10.0, "CUPON", true, java.time.LocalDate.now().plusDays(30));
        when(descuentoRepository.save(nuevo)).thenReturn(guardado);
        Descuento resultado = descuentoService.guardarDescuento(nuevo);
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(descuentoRepository).save(nuevo);
    }

    @Test
    void testListarDescuentos() {
        List<Descuento> lista = Arrays.asList(new Descuento(1L, "CUPON01", "Descuento verano", 10.0, "CUPON", true, java.time.LocalDate.now().plusDays(30)));
        when(descuentoRepository.findAll()).thenReturn(lista);
        List<Descuento> resultado = descuentoService.listarDescuentos();
        assertThat(resultado).hasSize(1);
        verify(descuentoRepository).findAll();
    }

    @Test
    void testObtenerDescuentoPorIdExistente() {
        Descuento obj = new Descuento(1L, "CUPON01", "Descuento verano", 10.0, "CUPON", true, java.time.LocalDate.now().plusDays(30));
        when(descuentoRepository.findById(1L)).thenReturn(Optional.of(obj));
        Optional<Descuento> resultado = descuentoService.obtenerDescuentoPorId(1L);
        assertThat(resultado).isPresent();
        verify(descuentoRepository).findById(1L);
    }

    @Test
    void testObtenerDescuentoPorIdNoExistente() {
        when(descuentoRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Descuento> resultado = descuentoService.obtenerDescuentoPorId(99L);
        assertThat(resultado).isEmpty();
        verify(descuentoRepository).findById(99L);
    }

    @Test
    void testActualizarDescuento() {
        Descuento existente = new Descuento(1L, "CUPON01", "Descuento verano", 10.0, "CUPON", true, java.time.LocalDate.now().plusDays(30));
        Descuento nuevo = new Descuento(null, "CUPON02", "Descuento invierno", 20.0, "PROMOCION", false, java.time.LocalDate.now().plusDays(60));
        when(descuentoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(descuentoRepository.save(any(Descuento.class))).thenAnswer(i -> i.getArgument(0));
        Descuento resultado = descuentoService.actualizarDescuento(1L, nuevo);
        assertThat(resultado).isNotNull();
        verify(descuentoRepository).save(existente);
    }

    @Test
    void testActualizarDescuentoNoExistente() {
        Descuento nuevo = new Descuento(null, "CUPON02", "Descuento invierno", 20.0, "PROMOCION", false, java.time.LocalDate.now().plusDays(60));
        when(descuentoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> descuentoService.actualizarDescuento(99L, nuevo));
    }

    @Test
    void testEliminarDescuento() {
        doNothing().when(descuentoRepository).deleteById(1L);
        descuentoService.eliminarDescuento(1L);
        verify(descuentoRepository).deleteById(1L);
    }

    // ─── Validacion: los datos no pueden estar nulos, vacios ni en blanco ──────

    @Test
    void testNoGuardaDescuentoConCodigoNuloVacioOEnBlanco() {
        for (String invalido : new String[]{null, "", "   "}) {
            Descuento d = new Descuento(null, invalido, "Descuento verano", 10.0, "CUPON", true, java.time.LocalDate.now().plusDays(30));
            assertThrows(IllegalArgumentException.class, () -> descuentoService.guardarDescuento(d));
        }
        verify(descuentoRepository, never()).save(any());
    }

    @Test
    void testNoGuardaDescuentoConTipoInvalido() {
        Descuento d = new Descuento(null, "CUPON01", "Descuento verano", 10.0, "OTRO", true, java.time.LocalDate.now().plusDays(30));
        assertThrows(IllegalArgumentException.class, () -> descuentoService.guardarDescuento(d));
        verify(descuentoRepository, never()).save(any());
    }
}
