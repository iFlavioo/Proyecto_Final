package com.bookpoint.sucursal.service;

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

import com.bookpoint.sucursal.model.Sucursal;
import com.bookpoint.sucursal.repository.SucursalRepository;

public class SucursalServiceTest {
    @Mock private SucursalRepository sucursalRepository;
    @InjectMocks private SucursalService sucursalService;

    @BeforeEach void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void testGuardarSucursal() {
        Sucursal nuevo = new Sucursal(null, "BookPoint Concepcion", "Barros Arana 123", "Concepcion", "412345678", "9:00-18:00");
        Sucursal guardado = new Sucursal(1L, "BookPoint Concepcion", "Barros Arana 123", "Concepcion", "412345678", "9:00-18:00");
        when(sucursalRepository.save(nuevo)).thenReturn(guardado);
        Sucursal resultado = sucursalService.guardarSucursal(nuevo);
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(sucursalRepository).save(nuevo);
    }

    @Test
    void testListarSucursals() {
        List<Sucursal> lista = Arrays.asList(new Sucursal(1L, "BookPoint Concepcion", "Barros Arana 123", "Concepcion", "412345678", "9:00-18:00"));
        when(sucursalRepository.findAll()).thenReturn(lista);
        List<Sucursal> resultado = sucursalService.listarSucursals();
        assertThat(resultado).hasSize(1);
        verify(sucursalRepository).findAll();
    }

    @Test
    void testObtenerSucursalPorIdExistente() {
        Sucursal obj = new Sucursal(1L, "BookPoint Concepcion", "Barros Arana 123", "Concepcion", "412345678", "9:00-18:00");
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(obj));
        Optional<Sucursal> resultado = sucursalService.obtenerSucursalPorId(1L);
        assertThat(resultado).isPresent();
        verify(sucursalRepository).findById(1L);
    }

    @Test
    void testObtenerSucursalPorIdNoExistente() {
        when(sucursalRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Sucursal> resultado = sucursalService.obtenerSucursalPorId(99L);
        assertThat(resultado).isEmpty();
        verify(sucursalRepository).findById(99L);
    }

    @Test
    void testActualizarSucursal() {
        Sucursal existente = new Sucursal(1L, "BookPoint Concepcion", "Barros Arana 123", "Concepcion", "412345678", "9:00-18:00");
        Sucursal nuevo = new Sucursal(null, "BookPoint Temuco", "Av Alemania 456", "Temuco", "452345678", "10:00-19:00");
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(sucursalRepository.save(any(Sucursal.class))).thenAnswer(i -> i.getArgument(0));
        Sucursal resultado = sucursalService.actualizarSucursal(1L, nuevo);
        assertThat(resultado).isNotNull();
        verify(sucursalRepository).save(existente);
    }

    @Test
    void testActualizarSucursalNoExistente() {
        Sucursal nuevo = new Sucursal(null, "BookPoint Temuco", "Av Alemania 456", "Temuco", "452345678", "10:00-19:00");
        when(sucursalRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> sucursalService.actualizarSucursal(99L, nuevo));
    }

    @Test
    void testEliminarSucursal() {
        doNothing().when(sucursalRepository).deleteById(1L);
        sucursalService.eliminarSucursal(1L);
        verify(sucursalRepository).deleteById(1L);
    }

    // ─── Validacion: los datos no pueden estar nulos, vacios ni en blanco ──────

    @Test
    void testNoGuardaSucursalConNombreNuloVacioOEnBlanco() {
        for (String invalido : new String[]{null, "", "   "}) {
            Sucursal s = new Sucursal(null, invalido, "Barros Arana 123", "Concepcion", "412345678", "9:00-18:00");
            assertThrows(IllegalArgumentException.class, () -> sucursalService.guardarSucursal(s));
        }
        verify(sucursalRepository, never()).save(any());
    }

    @Test
    void testNoGuardaSucursalConDireccionNulaVaciaOEnBlanco() {
        for (String invalido : new String[]{null, "", "   "}) {
            Sucursal s = new Sucursal(null, "BookPoint Concepcion", invalido, "Concepcion", "412345678", "9:00-18:00");
            assertThrows(IllegalArgumentException.class, () -> sucursalService.guardarSucursal(s));
        }
        verify(sucursalRepository, never()).save(any());
    }

    @Test
    void testNoGuardaSucursalConCiudadNulaVaciaOEnBlanco() {
        for (String invalido : new String[]{null, "", "   "}) {
            Sucursal s = new Sucursal(null, "BookPoint Concepcion", "Barros Arana 123", invalido, "412345678", "9:00-18:00");
            assertThrows(IllegalArgumentException.class, () -> sucursalService.guardarSucursal(s));
        }
        verify(sucursalRepository, never()).save(any());
    }

    @Test
    void testNoGuardaSucursalConCiudadInvalida() {
        Sucursal s = new Sucursal(null, "BookPoint Santiago", "Alameda 100", "Santiago", "412345678", "9:00-18:00");
        assertThrows(IllegalArgumentException.class, () -> sucursalService.guardarSucursal(s));
        verify(sucursalRepository, never()).save(any());
    }

    @Test
    void testNoGuardaSucursalConTelefonoInvalido() {
        Sucursal s = new Sucursal(null, "BookPoint Concepcion", "Barros Arana 123", "Concepcion", "123", "9:00-18:00");
        assertThrows(IllegalArgumentException.class, () -> sucursalService.guardarSucursal(s));
        verify(sucursalRepository, never()).save(any());
    }
}
