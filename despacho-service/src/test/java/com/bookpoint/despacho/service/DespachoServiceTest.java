package com.bookpoint.despacho.service;

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

import com.bookpoint.despacho.model.Despacho;
import com.bookpoint.despacho.repository.DespachoRepository;

public class DespachoServiceTest {

    @Mock private DespachoRepository despachoRepository;
    @Mock private RestTemplate restTemplate;

    private DespachoService despachoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        despachoService = new DespachoService();
        ReflectionTestUtils.setField(despachoService, "despachoRepository", despachoRepository);
        ReflectionTestUtils.setField(despachoService, "restTemplate", restTemplate);
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
    void testGuardarDespacho() {
        Despacho nuevo = new Despacho(null, 1L, 1L, "PREPARANDO", java.time.LocalDate.now(), "Av Siempre Viva 742");
        Despacho guardado = new Despacho(1L, 1L, 1L, "PREPARANDO", java.time.LocalDate.now(), "Av Siempre Viva 742");
        mockExisteTrue("/pedidos/");
        mockExisteTrue("/sucursales/");
        when(despachoRepository.save(nuevo)).thenReturn(guardado);
        Despacho resultado = despachoService.guardarDespacho(nuevo);
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(despachoRepository).save(nuevo);
    }

    @Test
    void testGuardarDespachoConPedidoInvalido() {
        Despacho nuevo = new Despacho(null, 1L, 1L, "PREPARANDO", java.time.LocalDate.now(), "Av Siempre Viva 742");
        mockExisteTrue("/sucursales/");
        mockExisteFalse("/pedidos/");
        assertThrows(RuntimeException.class, () -> despachoService.guardarDespacho(nuevo));
        verify(despachoRepository, never()).save(any());
    }

    @Test
    void testGuardarDespachoConSucursalInvalido() {
        Despacho nuevo = new Despacho(null, 1L, 1L, "PREPARANDO", java.time.LocalDate.now(), "Av Siempre Viva 742");
        mockExisteTrue("/pedidos/");
        mockExisteFalse("/sucursales/");
        assertThrows(RuntimeException.class, () -> despachoService.guardarDespacho(nuevo));
        verify(despachoRepository, never()).save(any());
    }

    @Test
    void testListarDespachos() {
        List<Despacho> lista = Arrays.asList(new Despacho(1L, 1L, 1L, "PREPARANDO", java.time.LocalDate.now(), "Av Siempre Viva 742"));
        when(despachoRepository.findAll()).thenReturn(lista);
        List<Despacho> resultado = despachoService.listarDespachos();
        assertThat(resultado).hasSize(1);
        verify(despachoRepository).findAll();
    }

    @Test
    void testObtenerDespachoPorIdExistente() {
        Despacho obj = new Despacho(1L, 1L, 1L, "PREPARANDO", java.time.LocalDate.now(), "Av Siempre Viva 742");
        when(despachoRepository.findById(1L)).thenReturn(Optional.of(obj));
        Optional<Despacho> resultado = despachoService.obtenerDespachoPorId(1L);
        assertThat(resultado).isPresent();
        verify(despachoRepository).findById(1L);
    }

    @Test
    void testObtenerDespachoPorIdNoExistente() {
        when(despachoRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Despacho> resultado = despachoService.obtenerDespachoPorId(99L);
        assertThat(resultado).isEmpty();
        verify(despachoRepository).findById(99L);
    }

    @Test
    void testActualizarDespacho() {
        Despacho existente = new Despacho(1L, 1L, 1L, "PREPARANDO", java.time.LocalDate.now(), "Av Siempre Viva 742");
        Despacho nuevo = new Despacho(null, 1L, 1L, "EN_CAMINO", java.time.LocalDate.now(), "Calle Falsa 123");
        mockExisteTrue("/pedidos/");
        mockExisteTrue("/sucursales/");
        when(despachoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(despachoRepository.save(any(Despacho.class))).thenAnswer(i -> i.getArgument(0));
        Despacho resultado = despachoService.actualizarDespacho(1L, nuevo);
        assertThat(resultado).isNotNull();
        verify(despachoRepository).save(existente);
    }

    @Test
    void testActualizarDespachoNoExistente() {
        Despacho nuevo = new Despacho(null, 1L, 1L, "EN_CAMINO", java.time.LocalDate.now(), "Calle Falsa 123");
        when(despachoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> despachoService.actualizarDespacho(99L, nuevo));
    }

    @Test
    void testEliminarDespacho() {
        doNothing().when(despachoRepository).deleteById(1L);
        despachoService.eliminarDespacho(1L);
        verify(despachoRepository).deleteById(1L);
    }

    // ─── Validacion: los datos no pueden estar nulos, vacios ni en blanco ──────

    @Test
    void testNoGuardaDespachoConDireccionDestinoNulaVaciaOEnBlanco() {
        for (String invalido : new String[]{null, "", "   "}) {
            Despacho d = new Despacho(null, 1L, 1L, "PREPARANDO", java.time.LocalDate.now(), invalido);
            assertThrows(IllegalArgumentException.class, () -> despachoService.guardarDespacho(d));
        }
        verify(despachoRepository, never()).save(any());
    }

    @Test
    void testNoGuardaDespachoConEstadoInvalido() {
        Despacho d = new Despacho(null, 1L, 1L, "VOLANDO", java.time.LocalDate.now(), "Av Siempre Viva 742");
        assertThrows(IllegalArgumentException.class, () -> despachoService.guardarDespacho(d));
        verify(despachoRepository, never()).save(any());
    }
}
