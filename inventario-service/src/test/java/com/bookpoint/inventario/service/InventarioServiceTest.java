package com.bookpoint.inventario.service;

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

import com.bookpoint.inventario.model.Inventario;
import com.bookpoint.inventario.repository.InventarioRepository;

public class InventarioServiceTest {

    @Mock private InventarioRepository inventarioRepository;
    @Mock private RestTemplate restTemplate;

    private InventarioService inventarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventarioService = new InventarioService();
        ReflectionTestUtils.setField(inventarioService, "inventarioRepository", inventarioRepository);
        ReflectionTestUtils.setField(inventarioService, "restTemplate", restTemplate);
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
    void testGuardarInventario() {
        Inventario nuevo = new Inventario(null, 1L, 1L, 50, 5);
        Inventario guardado = new Inventario(1L, 1L, 1L, 50, 5);
        mockExisteTrue("/productos/");
        mockExisteTrue("/sucursales/");
        when(inventarioRepository.save(nuevo)).thenReturn(guardado);
        Inventario resultado = inventarioService.guardarInventario(nuevo);
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(inventarioRepository).save(nuevo);
    }

    @Test
    void testGuardarInventarioConProductoInvalido() {
        Inventario nuevo = new Inventario(null, 1L, 1L, 50, 5);
        mockExisteTrue("/sucursales/");
        mockExisteFalse("/productos/");
        assertThrows(RuntimeException.class, () -> inventarioService.guardarInventario(nuevo));
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    void testGuardarInventarioConSucursalInvalido() {
        Inventario nuevo = new Inventario(null, 1L, 1L, 50, 5);
        mockExisteTrue("/productos/");
        mockExisteFalse("/sucursales/");
        assertThrows(RuntimeException.class, () -> inventarioService.guardarInventario(nuevo));
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    void testListarInventarios() {
        List<Inventario> lista = Arrays.asList(new Inventario(1L, 1L, 1L, 50, 5));
        when(inventarioRepository.findAll()).thenReturn(lista);
        List<Inventario> resultado = inventarioService.listarInventarios();
        assertThat(resultado).hasSize(1);
        verify(inventarioRepository).findAll();
    }

    @Test
    void testObtenerInventarioPorIdExistente() {
        Inventario obj = new Inventario(1L, 1L, 1L, 50, 5);
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(obj));
        Optional<Inventario> resultado = inventarioService.obtenerInventarioPorId(1L);
        assertThat(resultado).isPresent();
        verify(inventarioRepository).findById(1L);
    }

    @Test
    void testObtenerInventarioPorIdNoExistente() {
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Inventario> resultado = inventarioService.obtenerInventarioPorId(99L);
        assertThat(resultado).isEmpty();
        verify(inventarioRepository).findById(99L);
    }

    @Test
    void testActualizarInventario() {
        Inventario existente = new Inventario(1L, 1L, 1L, 50, 5);
        Inventario nuevo = new Inventario(null, 1L, 1L, 30, 10);
        mockExisteTrue("/productos/");
        mockExisteTrue("/sucursales/");
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(inventarioRepository.save(any(Inventario.class))).thenAnswer(i -> i.getArgument(0));
        Inventario resultado = inventarioService.actualizarInventario(1L, nuevo);
        assertThat(resultado).isNotNull();
        verify(inventarioRepository).save(existente);
    }

    @Test
    void testActualizarInventarioNoExistente() {
        Inventario nuevo = new Inventario(null, 1L, 1L, 30, 10);
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> inventarioService.actualizarInventario(99L, nuevo));
    }

    @Test
    void testEliminarInventario() {
        doNothing().when(inventarioRepository).deleteById(1L);
        inventarioService.eliminarInventario(1L);
        verify(inventarioRepository).deleteById(1L);
    }

    // ─── descontarStock (usado por venta-service) ──────────────────────────

    @Test
    void testDescontarStockExitoso() {
        Inventario inv = new Inventario(1L, 1L, 1L, 50, 5);
        when(inventarioRepository.findByProductoIdAndSucursalId(1L, 1L)).thenReturn(Optional.of(inv));
        when(inventarioRepository.save(any(Inventario.class))).thenAnswer(i -> i.getArgument(0));

        inventarioService.descontarStock(1L, 1L, 10);

        assertThat(inv.getStock()).isEqualTo(40);
        verify(inventarioRepository).save(inv);
    }

    @Test
    void testDescontarStockInsuficiente() {
        Inventario inv = new Inventario(1L, 1L, 1L, 5, 5);
        when(inventarioRepository.findByProductoIdAndSucursalId(1L, 1L)).thenReturn(Optional.of(inv));

        assertThrows(RuntimeException.class, () -> inventarioService.descontarStock(1L, 1L, 10));
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    void testDescontarStockSinInventarioRegistrado() {
        when(inventarioRepository.findByProductoIdAndSucursalId(99L, 99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> inventarioService.descontarStock(99L, 99L, 1));
    }

    // ─── consultarStock (usado por venta-service) ──────────────────────────

    @Test
    void testConsultarStockExistente() {
        Inventario inv = new Inventario(1L, 1L, 1L, 50, 5);
        when(inventarioRepository.findByProductoIdAndSucursalId(1L, 1L)).thenReturn(Optional.of(inv));

        Optional<Inventario> resultado = inventarioService.consultarStock(1L, 1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getStock()).isEqualTo(50);
    }

    @Test
    void testConsultarStockNoExistente() {
        when(inventarioRepository.findByProductoIdAndSucursalId(99L, 99L)).thenReturn(Optional.empty());

        Optional<Inventario> resultado = inventarioService.consultarStock(99L, 99L);

        assertThat(resultado).isEmpty();
    }
}
