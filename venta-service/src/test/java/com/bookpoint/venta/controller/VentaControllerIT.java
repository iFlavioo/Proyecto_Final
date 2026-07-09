package com.bookpoint.venta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.venta.dto.VentaDTO;
import com.bookpoint.venta.dto.VentaDetalleDTO;
import com.bookpoint.venta.repository.VentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class VentaControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private VentaRepository ventaRepository;
    @Autowired private ObjectMapper objectMapper;

    @SuppressWarnings("removal")
    @MockBean private RestTemplate restTemplate;

    @BeforeEach
    void limpiar() {
        ventaRepository.deleteAll();
        // Usuario y sucursal existen por defecto
        when(restTemplate.getForEntity(contains("/usuarios/"), eq(String.class)))
            .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
        when(restTemplate.getForEntity(contains("/sucursales/"), eq(String.class)))
            .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
        // Producto existe con precio 15990
        when(restTemplate.getForEntity(contains("/productos/"), eq(String.class)))
            .thenReturn(new ResponseEntity<>("{\"id\":1,\"precio\":15990.0}", HttpStatus.OK));
        // Inventario con stock suficiente
        when(restTemplate.getForEntity(contains("/inventario/consultar"), eq(String.class)))
            .thenReturn(new ResponseEntity<>("{\"stock\":50,\"stockMinimo\":5}", HttpStatus.OK));
        // Descuento de stock exitoso
        when(restTemplate.exchange(contains("/descontar"), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Void.class)))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));
    }

    private VentaDTO buildDTO() {
        VentaDetalleDTO d1 = new VentaDetalleDTO(null, 1L, 2, null, null);
        VentaDetalleDTO d2 = new VentaDetalleDTO(null, 2L, 1, null, null);
        return new VentaDTO(null, 1L, 1L, LocalDate.now(), null, List.of(d1, d2));
    }

    @Test
    void testCrearVentaConMultiplesProductosCalculaTotalAutomaticamente() throws Exception {
        mockMvc.perform(post("/api/v1/ventas").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildDTO())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.total").value(47970.0)) // (15990*2) + (15990*1) = 47970
            .andExpect(jsonPath("$.detalles").isArray());
    }

    @Test
    void testCrearVentaConProductoInexistenteFalla() throws Exception {
        when(restTemplate.getForEntity(contains("/productos/"), eq(String.class)))
            .thenThrow(new RestClientException("404 Not Found"));
        mockMvc.perform(post("/api/v1/ventas").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildDTO())))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testCrearVentaConUsuarioInexistenteFalla() throws Exception {
        when(restTemplate.getForEntity(contains("/usuarios/"), eq(String.class)))
            .thenThrow(new RestClientException("404 Not Found"));
        mockMvc.perform(post("/api/v1/ventas").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildDTO())))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testCrearVentaConStockInsuficienteFalla() throws Exception {
        when(restTemplate.getForEntity(contains("/inventario/consultar"), eq(String.class)))
            .thenReturn(new ResponseEntity<>("{\"stock\":0,\"stockMinimo\":5}", HttpStatus.OK));
        mockMvc.perform(post("/api/v1/ventas").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildDTO())))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testListarVentas() throws Exception {
        mockMvc.perform(post("/api/v1/ventas").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildDTO()))).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/ventas")).andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void testEliminarVenta() throws Exception {
        String resp = mockMvc.perform(post("/api/v1/ventas").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildDTO())))
            .andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readTree(resp).get("id").asLong();
        mockMvc.perform(delete("/api/v1/ventas/" + id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/ventas/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        mockMvc.perform(get("/api/v1/ventas/9999")).andExpect(status().isNotFound());
    }
}
