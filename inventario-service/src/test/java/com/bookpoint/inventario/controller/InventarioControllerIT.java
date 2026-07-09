package com.bookpoint.inventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.inventario.dto.InventarioDTO;
import com.bookpoint.inventario.model.Inventario;
import com.bookpoint.inventario.repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class InventarioControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private InventarioRepository inventarioRepository;
    @Autowired private ObjectMapper objectMapper;

    @SuppressWarnings("removal")
    @MockBean private RestTemplate restTemplate;

    @BeforeEach
    void limpiar() {
        inventarioRepository.deleteAll();
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
            .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
    }

    @Test
    void testCrearYListar() throws Exception {
        InventarioDTO dto = new InventarioDTO(null, 1L, 1L, 50, 5);
        mockMvc.perform(post("/api/v1/inventario").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").exists());
        mockMvc.perform(get("/api/v1/inventario")).andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void testCrearConIdExternoInvalido() throws Exception {
        when(restTemplate.getForEntity(contains("/productos/"), eq(String.class)))
            .thenThrow(new RestClientException("404 Not Found"));
        InventarioDTO dto = new InventarioDTO(null, 1L, 1L, 50, 5);
        mockMvc.perform(post("/api/v1/inventario").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testCrearConDatosInvalidosRetorna400() throws Exception {
        mockMvc.perform(post("/api/v1/inventario").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testEliminar() throws Exception {
        Inventario guardado = inventarioRepository.save(new Inventario(null, 1L, 1L, 50, 5));
        mockMvc.perform(delete("/api/v1/inventario/"+guardado.getId())).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/inventario/"+guardado.getId())).andExpect(status().isNotFound());
    }

    @Test
    void testActualizar() throws Exception {
        Inventario guardado = inventarioRepository.save(new Inventario(null, 1L, 1L, 50, 5));
        InventarioDTO dto = new InventarioDTO(null, 1L, 1L, 30, 10);
        mockMvc.perform(put("/api/v1/inventario/"+guardado.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        mockMvc.perform(get("/api/v1/inventario/9999")).andExpect(status().isNotFound());
    }

    @Test
    void testConsultarYDescontarStock() throws Exception {
        Inventario guardado = inventarioRepository.save(new Inventario(null, 1L, 1L, 50, 5));

        mockMvc.perform(get("/api/v1/inventario/consultar?productoId=1&sucursalId=1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.stock").value(50));

        mockMvc.perform(put("/api/v1/inventario/descontar").contentType(MediaType.APPLICATION_JSON)
                .content("{\"productoId\":1,\"sucursalId\":1,\"cantidad\":10}"))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/inventario/consultar?productoId=1&sucursalId=1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.stock").value(40));
    }

    @Test
    void testConsultarStockNoExistente() throws Exception {
        mockMvc.perform(get("/api/v1/inventario/consultar?productoId=999&sucursalId=999"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDescontarStockInsuficienteFalla() throws Exception {
        inventarioRepository.save(new Inventario(null, 1L, 1L, 5, 5));
        mockMvc.perform(put("/api/v1/inventario/descontar").contentType(MediaType.APPLICATION_JSON)
                .content("{\"productoId\":1,\"sucursalId\":1,\"cantidad\":999}"))
            .andExpect(status().isBadRequest());
    }
}
