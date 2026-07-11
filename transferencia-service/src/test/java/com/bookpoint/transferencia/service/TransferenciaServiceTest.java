package com.bookpoint.transferencia.service;

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

import com.bookpoint.transferencia.model.Transferencia;
import com.bookpoint.transferencia.repository.TransferenciaRepository;

public class TransferenciaServiceTest {

    @Mock private TransferenciaRepository transferenciaRepository;
    @Mock private RestTemplate restTemplate;

    private TransferenciaService transferenciaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transferenciaService = new TransferenciaService();
        ReflectionTestUtils.setField(transferenciaService, "transferenciaRepository", transferenciaRepository);
        ReflectionTestUtils.setField(transferenciaService, "restTemplate", restTemplate);
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
    void testGuardarTransferencia() {
        Transferencia nuevo = new Transferencia(null, 1L, 1L, 2L, 5, "PENDIENTE", java.time.LocalDate.now());
        Transferencia guardado = new Transferencia(1L, 1L, 1L, 2L, 5, "PENDIENTE", java.time.LocalDate.now());
        mockExisteTrue("/productos/");
        mockExisteTrue("/sucursales/");
        when(transferenciaRepository.save(nuevo)).thenReturn(guardado);
        Transferencia resultado = transferenciaService.guardarTransferencia(nuevo);
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(transferenciaRepository).save(nuevo);
    }

    @Test
    void testGuardarTransferenciaConProductoInvalido() {
        Transferencia nuevo = new Transferencia(null, 1L, 1L, 2L, 5, "PENDIENTE", java.time.LocalDate.now());
        mockExisteTrue("/sucursales/");
        mockExisteFalse("/productos/");
        assertThrows(RuntimeException.class, () -> transferenciaService.guardarTransferencia(nuevo));
        verify(transferenciaRepository, never()).save(any());
    }

    @Test
    void testGuardarTransferenciaConSucursalInvalido() {
        Transferencia nuevo = new Transferencia(null, 1L, 1L, 2L, 5, "PENDIENTE", java.time.LocalDate.now());
        mockExisteTrue("/productos/");
        mockExisteFalse("/sucursales/");
        assertThrows(RuntimeException.class, () -> transferenciaService.guardarTransferencia(nuevo));
        verify(transferenciaRepository, never()).save(any());
    }

    @Test
    void testListarTransferencias() {
        List<Transferencia> lista = Arrays.asList(new Transferencia(1L, 1L, 1L, 2L, 5, "PENDIENTE", java.time.LocalDate.now()));
        when(transferenciaRepository.findAll()).thenReturn(lista);
        List<Transferencia> resultado = transferenciaService.listarTransferencias();
        assertThat(resultado).hasSize(1);
        verify(transferenciaRepository).findAll();
    }

    @Test
    void testObtenerTransferenciaPorIdExistente() {
        Transferencia obj = new Transferencia(1L, 1L, 1L, 2L, 5, "PENDIENTE", java.time.LocalDate.now());
        when(transferenciaRepository.findById(1L)).thenReturn(Optional.of(obj));
        Optional<Transferencia> resultado = transferenciaService.obtenerTransferenciaPorId(1L);
        assertThat(resultado).isPresent();
        verify(transferenciaRepository).findById(1L);
    }

    @Test
    void testObtenerTransferenciaPorIdNoExistente() {
        when(transferenciaRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Transferencia> resultado = transferenciaService.obtenerTransferenciaPorId(99L);
        assertThat(resultado).isEmpty();
        verify(transferenciaRepository).findById(99L);
    }

    @Test
    void testActualizarTransferencia() {
        Transferencia existente = new Transferencia(1L, 1L, 1L, 2L, 5, "PENDIENTE", java.time.LocalDate.now());
        Transferencia nuevo = new Transferencia(null, 1L, 1L, 2L, 10, "APROBADA", java.time.LocalDate.now());
        mockExisteTrue("/productos/");
        mockExisteTrue("/sucursales/");
        when(transferenciaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(transferenciaRepository.save(any(Transferencia.class))).thenAnswer(i -> i.getArgument(0));
        Transferencia resultado = transferenciaService.actualizarTransferencia(1L, nuevo);
        assertThat(resultado).isNotNull();
        verify(transferenciaRepository).save(existente);
    }

    @Test
    void testActualizarTransferenciaNoExistente() {
        Transferencia nuevo = new Transferencia(null, 1L, 1L, 2L, 10, "APROBADA", java.time.LocalDate.now());
        when(transferenciaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> transferenciaService.actualizarTransferencia(99L, nuevo));
    }

    @Test
    void testEliminarTransferencia() {
        doNothing().when(transferenciaRepository).deleteById(1L);
        transferenciaService.eliminarTransferencia(1L);
        verify(transferenciaRepository).deleteById(1L);
    }

    // ─── Validacion: los datos no pueden estar nulos, vacios ni en blanco ──────

    @Test
    void testNoGuardaTransferenciaConEstadoInvalido() {
        Transferencia t = new Transferencia(null, 1L, 1L, 2L, 5, "OTRO", java.time.LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> transferenciaService.guardarTransferencia(t));
        verify(transferenciaRepository, never()).save(any());
    }
}
