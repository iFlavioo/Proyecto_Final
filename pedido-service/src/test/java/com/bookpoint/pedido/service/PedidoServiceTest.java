package com.bookpoint.pedido.service;

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

import com.bookpoint.pedido.model.Pedido;
import com.bookpoint.pedido.repository.PedidoRepository;

public class PedidoServiceTest {

    @Mock private PedidoRepository pedidoRepository;
    @Mock private RestTemplate restTemplate;

    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pedidoService = new PedidoService();
        ReflectionTestUtils.setField(pedidoService, "pedidoRepository", pedidoRepository);
        ReflectionTestUtils.setField(pedidoService, "restTemplate", restTemplate);
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
    void testGuardarPedido() {
        Pedido nuevo = new Pedido(null, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742");
        Pedido guardado = new Pedido(1L, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742");
        mockExisteTrue("/usuarios/");
        mockExisteTrue("/productos/");
        when(pedidoRepository.save(nuevo)).thenReturn(guardado);
        Pedido resultado = pedidoService.guardarPedido(nuevo);
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(pedidoRepository).save(nuevo);
    }

    @Test
    void testGuardarPedidoConUsuarioInvalido() {
        Pedido nuevo = new Pedido(null, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742");
        mockExisteTrue("/productos/");
        mockExisteFalse("/usuarios/");
        assertThrows(RuntimeException.class, () -> pedidoService.guardarPedido(nuevo));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void testGuardarPedidoConProductoInvalido() {
        Pedido nuevo = new Pedido(null, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742");
        mockExisteTrue("/usuarios/");
        mockExisteFalse("/productos/");
        assertThrows(RuntimeException.class, () -> pedidoService.guardarPedido(nuevo));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void testListarPedidos() {
        List<Pedido> lista = Arrays.asList(new Pedido(1L, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742"));
        when(pedidoRepository.findAll()).thenReturn(lista);
        List<Pedido> resultado = pedidoService.listarPedidos();
        assertThat(resultado).hasSize(1);
        verify(pedidoRepository).findAll();
    }

    @Test
    void testObtenerPedidoPorIdExistente() {
        Pedido obj = new Pedido(1L, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742");
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(obj));
        Optional<Pedido> resultado = pedidoService.obtenerPedidoPorId(1L);
        assertThat(resultado).isPresent();
        verify(pedidoRepository).findById(1L);
    }

    @Test
    void testObtenerPedidoPorIdNoExistente() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Pedido> resultado = pedidoService.obtenerPedidoPorId(99L);
        assertThat(resultado).isEmpty();
        verify(pedidoRepository).findById(99L);
    }

    @Test
    void testActualizarPedido() {
        Pedido existente = new Pedido(1L, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742");
        Pedido nuevo = new Pedido(null, 1L, 1L, 3, "ENVIADO", java.time.LocalDate.now(), "Calle Falsa 123");
        mockExisteTrue("/usuarios/");
        mockExisteTrue("/productos/");
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));
        Pedido resultado = pedidoService.actualizarPedido(1L, nuevo);
        assertThat(resultado).isNotNull();
        verify(pedidoRepository).save(existente);
    }

    @Test
    void testActualizarPedidoNoExistente() {
        Pedido nuevo = new Pedido(null, 1L, 1L, 3, "ENVIADO", java.time.LocalDate.now(), "Calle Falsa 123");
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> pedidoService.actualizarPedido(99L, nuevo));
    }

    @Test
    void testEliminarPedido() {
        doNothing().when(pedidoRepository).deleteById(1L);
        pedidoService.eliminarPedido(1L);
        verify(pedidoRepository).deleteById(1L);
    }

    // ─── Validacion: los datos no pueden estar nulos, vacios ni en blanco ──────

    @Test
    void testNoGuardaPedidoConEstadoNuloVacioOEnBlanco() {
        for (String invalido : new String[]{null, "", "   "}) {
            Pedido p = new Pedido(null, 1L, 1L, 2, invalido, java.time.LocalDate.now(), "Av Siempre Viva 742");
            assertThrows(IllegalArgumentException.class, () -> pedidoService.guardarPedido(p));
        }
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void testNoGuardaPedidoConEstadoInvalido() {
        Pedido p = new Pedido(null, 1L, 1L, 2, "VOLANDO", java.time.LocalDate.now(), "Av Siempre Viva 742");
        assertThrows(IllegalArgumentException.class, () -> pedidoService.guardarPedido(p));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void testNoGuardaPedidoConDireccionEntregaNulaVaciaOEnBlanco() {
        for (String invalido : new String[]{null, "", "   "}) {
            Pedido p = new Pedido(null, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), invalido);
            assertThrows(IllegalArgumentException.class, () -> pedidoService.guardarPedido(p));
        }
        verify(pedidoRepository, never()).save(any());
    }
}
