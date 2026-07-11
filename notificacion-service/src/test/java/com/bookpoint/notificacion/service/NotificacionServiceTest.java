package com.bookpoint.notificacion.service;

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

import com.bookpoint.notificacion.model.Notificacion;
import com.bookpoint.notificacion.repository.NotificacionRepository;

public class NotificacionServiceTest {

    @Mock private NotificacionRepository notificacionRepository;
    @Mock private RestTemplate restTemplate;

    private NotificacionService notificacionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificacionService = new NotificacionService();
        ReflectionTestUtils.setField(notificacionService, "notificacionRepository", notificacionRepository);
        ReflectionTestUtils.setField(notificacionService, "restTemplate", restTemplate);
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
    void testGuardarNotificacion() {
        Notificacion nuevo = new Notificacion(null, 1L, "Tu pedido ha sido confirmado", "PEDIDO", false, java.time.LocalDate.now());
        Notificacion guardado = new Notificacion(1L, 1L, "Tu pedido ha sido confirmado", "PEDIDO", false, java.time.LocalDate.now());
        mockExisteTrue("/usuarios/");
        when(notificacionRepository.save(nuevo)).thenReturn(guardado);
        Notificacion resultado = notificacionService.guardarNotificacion(nuevo);
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(notificacionRepository).save(nuevo);
    }

    @Test
    void testGuardarNotificacionConUsuarioInvalido() {
        Notificacion nuevo = new Notificacion(null, 1L, "Tu pedido ha sido confirmado", "PEDIDO", false, java.time.LocalDate.now());
        // sin otras dependencias
        mockExisteFalse("/usuarios/");
        assertThrows(RuntimeException.class, () -> notificacionService.guardarNotificacion(nuevo));
        verify(notificacionRepository, never()).save(any());
    }

    @Test
    void testListarNotificacions() {
        List<Notificacion> lista = Arrays.asList(new Notificacion(1L, 1L, "Tu pedido ha sido confirmado", "PEDIDO", false, java.time.LocalDate.now()));
        when(notificacionRepository.findAll()).thenReturn(lista);
        List<Notificacion> resultado = notificacionService.listarNotificacions();
        assertThat(resultado).hasSize(1);
        verify(notificacionRepository).findAll();
    }

    @Test
    void testObtenerNotificacionPorIdExistente() {
        Notificacion obj = new Notificacion(1L, 1L, "Tu pedido ha sido confirmado", "PEDIDO", false, java.time.LocalDate.now());
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(obj));
        Optional<Notificacion> resultado = notificacionService.obtenerNotificacionPorId(1L);
        assertThat(resultado).isPresent();
        verify(notificacionRepository).findById(1L);
    }

    @Test
    void testObtenerNotificacionPorIdNoExistente() {
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Notificacion> resultado = notificacionService.obtenerNotificacionPorId(99L);
        assertThat(resultado).isEmpty();
        verify(notificacionRepository).findById(99L);
    }

    @Test
    void testActualizarNotificacion() {
        Notificacion existente = new Notificacion(1L, 1L, "Tu pedido ha sido confirmado", "PEDIDO", false, java.time.LocalDate.now());
        Notificacion nuevo = new Notificacion(null, 1L, "Stock minimo alcanzado", "STOCK", true, java.time.LocalDate.now());
        mockExisteTrue("/usuarios/");
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(notificacionRepository.save(any(Notificacion.class))).thenAnswer(i -> i.getArgument(0));
        Notificacion resultado = notificacionService.actualizarNotificacion(1L, nuevo);
        assertThat(resultado).isNotNull();
        verify(notificacionRepository).save(existente);
    }

    @Test
    void testActualizarNotificacionNoExistente() {
        Notificacion nuevo = new Notificacion(null, 1L, "Stock minimo alcanzado", "STOCK", true, java.time.LocalDate.now());
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> notificacionService.actualizarNotificacion(99L, nuevo));
    }

    @Test
    void testEliminarNotificacion() {
        doNothing().when(notificacionRepository).deleteById(1L);
        notificacionService.eliminarNotificacion(1L);
        verify(notificacionRepository).deleteById(1L);
    }

    // ─── Validacion: los datos no pueden estar nulos, vacios ni en blanco ──────

    @Test
    void testNoGuardaNotificacionConMensajeNuloVacioOEnBlanco() {
        for (String invalido : new String[]{null, "", "   "}) {
            Notificacion n = new Notificacion(null, 1L, invalido, "PEDIDO", false, java.time.LocalDate.now());
            assertThrows(IllegalArgumentException.class, () -> notificacionService.guardarNotificacion(n));
        }
        verify(notificacionRepository, never()).save(any());
    }
}
