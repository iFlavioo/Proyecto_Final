package com.bookpoint.venta.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.bookpoint.venta.model.Venta;
import com.bookpoint.venta.model.VentaDetalle;
import com.bookpoint.venta.repository.VentaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VentaServiceTest {

    @Mock private VentaRepository ventaRepository;
    @Mock private RestTemplate restTemplate;

    private VentaService ventaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ventaService = new VentaService();
        ReflectionTestUtils.setField(ventaService, "ventaRepository", ventaRepository);
        ReflectionTestUtils.setField(ventaService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(ventaService, "objectMapper", new ObjectMapper());
        ReflectionTestUtils.setField(ventaService, "usuarioUrl", "http://localhost:8081");
        ReflectionTestUtils.setField(ventaService, "productoUrl", "http://localhost:8084");
        ReflectionTestUtils.setField(ventaService, "sucursalUrl", "http://localhost:8089");
        ReflectionTestUtils.setField(ventaService, "inventarioUrl", "http://localhost:8085");
    }

    private void mockOk(String urlSubstring, String jsonBody) {
        when(restTemplate.getForEntity(contains(urlSubstring), eq(String.class)))
            .thenReturn(new ResponseEntity<>(jsonBody, HttpStatus.OK));
    }

    private void mockFalla(String urlSubstring) {
        when(restTemplate.getForEntity(contains(urlSubstring), eq(String.class)))
            .thenThrow(new RestClientException("404 Not Found"));
    }

    private void mockDescontarOk() {
        when(restTemplate.exchange(contains("/descontar"), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Void.class)))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));
    }

    private Venta ventaConUnDetalle() {
        Venta v = new Venta();
        v.setUsuarioId(1L); v.setSucursalId(1L); v.setFechaVenta(LocalDate.now());
        VentaDetalle d = new VentaDetalle();
        d.setProductoId(1L); d.setCantidad(2);
        v.setDetalles(new ArrayList<>(Arrays.asList(d)));
        return v;
    }

    // ─── Camino feliz ────────────────────────────────────────────────────────

    @Test
    void testGuardarVentaConUnProductoCalculaPrecioAutomaticamente() {
        Venta nueva = ventaConUnDetalle();
        mockOk("/usuarios/", "{}");
        mockOk("/sucursales/", "{}");
        mockOk("/productos/", "{\"id\":1,\"nombre\":\"Libro\",\"precio\":15990.0}");
        mockOk("/inventario/consultar", "{\"id\":1,\"stock\":50,\"stockMinimo\":5}");
        mockDescontarOk();
        when(ventaRepository.save(any(Venta.class))).thenAnswer(i -> { Venta v = i.getArgument(0); v.setId(1L); return v; });

        Venta resultado = ventaService.guardarVenta(nueva);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getTotal()).isEqualTo(31980.0);
        assertThat(resultado.getDetalles().get(0).getPrecioUnitario()).isEqualTo(15990.0);
        verify(ventaRepository).save(any(Venta.class));
    }

    @Test
    void testGuardarVentaConMultiplesProductosSumaTotalCorrectamente() {
        Venta nueva = new Venta();
        nueva.setUsuarioId(1L); nueva.setSucursalId(1L); nueva.setFechaVenta(LocalDate.now());
        VentaDetalle d1 = new VentaDetalle(); d1.setProductoId(1L); d1.setCantidad(2);
        VentaDetalle d2 = new VentaDetalle(); d2.setProductoId(2L); d2.setCantidad(1);
        nueva.setDetalles(new ArrayList<>(Arrays.asList(d1, d2)));

        mockOk("/usuarios/", "{}");
        mockOk("/sucursales/", "{}");
        when(restTemplate.getForEntity(contains("/productos/1"), eq(String.class)))
            .thenReturn(new ResponseEntity<>("{\"precio\":10000.0}", HttpStatus.OK));
        when(restTemplate.getForEntity(contains("/productos/2"), eq(String.class)))
            .thenReturn(new ResponseEntity<>("{\"precio\":5000.0}", HttpStatus.OK));
        mockOk("/inventario/consultar", "{\"stock\":100,\"stockMinimo\":5}");
        mockDescontarOk();
        when(ventaRepository.save(any(Venta.class))).thenAnswer(i -> { Venta v = i.getArgument(0); v.setId(1L); return v; });

        Venta resultado = ventaService.guardarVenta(nueva);

        assertThat(resultado.getTotal()).isEqualTo(25000.0);
    }

    // ─── Validacion de IDs invalidos ────────────────────────────────────────

    @Test
    void testGuardarVentaUsuarioInvalido() {
        Venta nueva = ventaConUnDetalle();
        mockFalla("/usuarios/");
        assertThrows(RuntimeException.class, () -> ventaService.guardarVenta(nueva));
        verify(ventaRepository, never()).save(any());
    }

    @Test
    void testGuardarVentaSucursalInvalida() {
        Venta nueva = ventaConUnDetalle();
        mockOk("/usuarios/", "{}");
        mockFalla("/sucursales/");
        assertThrows(RuntimeException.class, () -> ventaService.guardarVenta(nueva));
        verify(ventaRepository, never()).save(any());
    }

    @Test
    void testGuardarVentaProductoInvalido() {
        Venta nueva = ventaConUnDetalle();
        mockOk("/usuarios/", "{}");
        mockOk("/sucursales/", "{}");
        mockFalla("/productos/");
        assertThrows(RuntimeException.class, () -> ventaService.guardarVenta(nueva));
        verify(ventaRepository, never()).save(any());
    }

    @Test
    void testGuardarVentaStockInsuficiente() {
        Venta nueva = ventaConUnDetalle();
        mockOk("/usuarios/", "{}");
        mockOk("/sucursales/", "{}");
        mockOk("/productos/", "{\"precio\":15990.0}");
        mockOk("/inventario/consultar", "{\"stock\":1,\"stockMinimo\":5}");
        assertThrows(RuntimeException.class, () -> ventaService.guardarVenta(nueva));
        verify(ventaRepository, never()).save(any());
    }

    @Test
    void testGuardarVentaSinInventarioRegistrado() {
        Venta nueva = ventaConUnDetalle();
        mockOk("/usuarios/", "{}");
        mockOk("/sucursales/", "{}");
        mockOk("/productos/", "{\"precio\":15990.0}");
        mockFalla("/inventario/consultar");
        assertThrows(RuntimeException.class, () -> ventaService.guardarVenta(nueva));
        verify(ventaRepository, never()).save(any());
    }

    @Test
    void testGuardarVentaProductoSinPrecioValido() {
        Venta nueva = ventaConUnDetalle();
        mockOk("/usuarios/", "{}");
        mockOk("/sucursales/", "{}");
        mockOk("/productos/", "{\"id\":1,\"nombre\":\"Libro sin precio\"}"); // sin campo "precio"
        assertThrows(RuntimeException.class, () -> ventaService.guardarVenta(nueva));
        verify(ventaRepository, never()).save(any());
    }

    @Test
    void testGuardarVentaFallaAlDescontarStock() {
        Venta nueva = ventaConUnDetalle();
        mockOk("/usuarios/", "{}");
        mockOk("/sucursales/", "{}");
        mockOk("/productos/", "{\"precio\":15990.0}");
        mockOk("/inventario/consultar", "{\"stock\":50,\"stockMinimo\":5}");
        when(ventaRepository.save(any(Venta.class))).thenAnswer(i -> { Venta v = i.getArgument(0); v.setId(1L); return v; });
        when(restTemplate.exchange(contains("/descontar"), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Void.class)))
            .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        assertThrows(RuntimeException.class, () -> ventaService.guardarVenta(nueva));
    }

    @Test
    void testGuardarVentaSinDetalles() {
        Venta nueva = new Venta();
        nueva.setUsuarioId(1L); nueva.setSucursalId(1L); nueva.setFechaVenta(LocalDate.now());
        nueva.setDetalles(new ArrayList<>());
        mockOk("/usuarios/", "{}");
        mockOk("/sucursales/", "{}");
        assertThrows(RuntimeException.class, () -> ventaService.guardarVenta(nueva));
        verify(ventaRepository, never()).save(any());
    }

    // ─── CRUD basico (sin llamadas externas) ────────────────────────────────

    @Test
    void testListarVentas() {
        Venta v = ventaConUnDetalle(); v.setId(1L);
        when(ventaRepository.findAll()).thenReturn(Arrays.asList(v));
        List<Venta> resultado = ventaService.listarVentas();
        assertThat(resultado).hasSize(1);
        verify(ventaRepository).findAll();
    }

    @Test
    void testObtenerVentaPorIdExistente() {
        Venta v = ventaConUnDetalle(); v.setId(1L);
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(v));
        Optional<Venta> resultado = ventaService.obtenerVentaPorId(1L);
        assertThat(resultado).isPresent();
        verify(ventaRepository).findById(1L);
    }

    @Test
    void testObtenerVentaPorIdNoExistente() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Venta> resultado = ventaService.obtenerVentaPorId(99L);
        assertThat(resultado).isEmpty();
        verify(ventaRepository).findById(99L);
    }

    @Test
    void testActualizarVenta() {
        Venta existente = ventaConUnDetalle(); existente.setId(1L);
        Venta nueva = ventaConUnDetalle(); nueva.setFechaVenta(LocalDate.now().minusDays(1));
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(i -> i.getArgument(0));
        Venta resultado = ventaService.actualizarVenta(1L, nueva);
        assertThat(resultado).isNotNull();
        verify(ventaRepository).save(existente);
    }

    @Test
    void testActualizarVentaNoExistente() {
        Venta nueva = ventaConUnDetalle();
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> ventaService.actualizarVenta(99L, nueva));
    }

    @Test
    void testEliminarVenta() {
        doNothing().when(ventaRepository).deleteById(1L);
        ventaService.eliminarVenta(1L);
        verify(ventaRepository).deleteById(1L);
    }
}
