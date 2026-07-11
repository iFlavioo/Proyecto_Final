package com.bookpoint.devolucion.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.bookpoint.devolucion.model.Devolucion;
import com.bookpoint.devolucion.repository.DevolucionRepository;

public class DevolucionServiceTest {

    @Mock private DevolucionRepository devolucionRepository;
    @Mock private RestTemplate restTemplate;

    private DevolucionService devolucionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        devolucionService = new DevolucionService();
        ReflectionTestUtils.setField(devolucionService, "devolucionRepository", devolucionRepository);
        ReflectionTestUtils.setField(devolucionService, "restTemplate", restTemplate);
    }

    private void mockExisteTrue(String urlSubstring) {
        when(restTemplate.getForEntity(contains(urlSubstring), eq(String.class)))
            .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
    }

    private void mockExisteFalse(String urlSubstring) {
        when(restTemplate.getForEntity(contains(urlSubstring), eq(String.class)))
            .thenThrow(new RestClientException("404 Not Found"));
    }

    @Test
    void testGuardarDevolucion() {
        Devolucion nuevo = new Devolucion(null, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now());
        Devolucion guardado = new Devolucion(1L, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now());
        mockExisteTrue("/usuarios/");
        mockExisteTrue("/productos/");
        when(devolucionRepository.save(nuevo)).thenReturn(guardado);
        Devolucion resultado = devolucionService.guardarDevolucion(nuevo);
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(devolucionRepository).save(nuevo);
    }

    @Test
    void testGuardarDevolucionConUsuarioInvalido() {
        Devolucion nuevo = new Devolucion(null, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now());
        mockExisteTrue("/productos/");
        mockExisteFalse("/usuarios/");
        assertThrows(RuntimeException.class, () -> devolucionService.guardarDevolucion(nuevo));
        verify(devolucionRepository, never()).save(any());
    }

    @Test
    void testGuardarDevolucionConProductoInvalido() {
        Devolucion nuevo = new Devolucion(null, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now());
        mockExisteTrue("/usuarios/");
        mockExisteFalse("/productos/");
        assertThrows(RuntimeException.class, () -> devolucionService.guardarDevolucion(nuevo));
        verify(devolucionRepository, never()).save(any());
    }

    @Test
    void testListarDevolucions() {
        List<Devolucion> lista = Arrays.asList(new Devolucion(1L, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now()));
        when(devolucionRepository.findAll()).thenReturn(lista);
        List<Devolucion> resultado = devolucionService.listarDevolucions();
        assertThat(resultado).hasSize(1);
        verify(devolucionRepository).findAll();
    }

    @Test
    void testObtenerDevolucionPorIdExistente() {
        Devolucion obj = new Devolucion(1L, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now());
        when(devolucionRepository.findById(1L)).thenReturn(Optional.of(obj));
        Optional<Devolucion> resultado = devolucionService.obtenerDevolucionPorId(1L);
        assertThat(resultado).isPresent();
        verify(devolucionRepository).findById(1L);
    }

    @Test
    void testObtenerDevolucionPorIdNoExistente() {
        when(devolucionRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Devolucion> resultado = devolucionService.obtenerDevolucionPorId(99L);
        assertThat(resultado).isEmpty();
        verify(devolucionRepository).findById(99L);
    }

    @Test
    void testActualizarDevolucion() {
        Devolucion existente = new Devolucion(1L, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now());
        Devolucion nuevo = new Devolucion(null, 1L, 1L, "Revisado y aprobado por tienda", "APROBADA", java.time.LocalDate.now());
        mockExisteTrue("/usuarios/");
        mockExisteTrue("/productos/");
        when(devolucionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(devolucionRepository.save(any(Devolucion.class))).thenAnswer(i -> i.getArgument(0));
        Devolucion resultado = devolucionService.actualizarDevolucion(1L, nuevo);
        assertThat(resultado).isNotNull();
        verify(devolucionRepository).save(existente);
    }

    @Test
    void testActualizarDevolucionNoExistente() {
        Devolucion nuevo = new Devolucion(null, 1L, 1L, "Revisado y aprobado por tienda", "APROBADA", java.time.LocalDate.now());
        when(devolucionRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> devolucionService.actualizarDevolucion(99L, nuevo));
    }

    @Test
    void testEliminarDevolucion() {
        doNothing().when(devolucionRepository).deleteById(1L);
        devolucionService.eliminarDevolucion(1L);
        verify(devolucionRepository).deleteById(1L);
    }

    // ─── Validacion: los datos no pueden estar nulos, vacios ni en blanco ──────

    @Test
    void testNoGuardaDevolucionConMotivoNuloVacioOEnBlanco() {
        for (String invalido : new String[]{null, "", "   "}) {
            Devolucion d = new Devolucion(null, 1L, 1L, invalido, "PENDIENTE", java.time.LocalDate.now());
            assertThrows(IllegalArgumentException.class, () -> devolucionService.guardarDevolucion(d));
        }
        verify(devolucionRepository, never()).save(any());
    }

    @Test
    void testNoGuardaDevolucionConEstadoNuloVacioOEnBlanco() {
        for (String invalido : new String[]{null, "", "   "}) {
            Devolucion d = new Devolucion(null, 1L, 1L, "Producto llego defectuoso", invalido, java.time.LocalDate.now());
            assertThrows(IllegalArgumentException.class, () -> devolucionService.guardarDevolucion(d));
        }
        verify(devolucionRepository, never()).save(any());
    }

    @Test
    void testNoGuardaDevolucionConEstadoInvalido() {
        Devolucion d = new Devolucion(null, 1L, 1L, "Producto llego defectuoso", "OTRO", java.time.LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> devolucionService.guardarDevolucion(d));
        verify(devolucionRepository, never()).save(any());
    }
}
