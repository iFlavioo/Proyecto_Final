package com.bookpoint.pedido.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.pedido.dto.PedidoDTO;
import com.bookpoint.pedido.model.Pedido;
import com.bookpoint.pedido.repository.PedidoRepository;
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
class PedidoControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private ObjectMapper objectMapper;

    @SuppressWarnings("removal")
    @MockBean private RestTemplate restTemplate;

    @BeforeEach
    void limpiar() {
        pedidoRepository.deleteAll();
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
            .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
    }

    @Test
    void testCrearYListar() throws Exception {
        PedidoDTO dto = new PedidoDTO(null, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742");
        mockMvc.perform(post("/api/v1/pedidos").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").exists());
        mockMvc.perform(get("/api/v1/pedidos")).andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void testCrearConIdExternoInvalido() throws Exception {
        when(restTemplate.getForEntity(contains("/usuarios/"), eq(String.class)))
            .thenThrow(new RestClientException("404 Not Found"));
        PedidoDTO dto = new PedidoDTO(null, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742");
        mockMvc.perform(post("/api/v1/pedidos").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testCrearConDatosInvalidosRetorna400() throws Exception {
        mockMvc.perform(post("/api/v1/pedidos").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testEliminar() throws Exception {
        Pedido guardado = pedidoRepository.save(new Pedido(null, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742"));
        mockMvc.perform(delete("/api/v1/pedidos/"+guardado.getId())).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/pedidos/"+guardado.getId())).andExpect(status().isNotFound());
    }

    @Test
    void testActualizar() throws Exception {
        Pedido guardado = pedidoRepository.save(new Pedido(null, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742"));
        PedidoDTO dto = new PedidoDTO(null, 1L, 1L, 3, "ENVIADO", java.time.LocalDate.now(), "Calle Falsa 123");
        mockMvc.perform(put("/api/v1/pedidos/"+guardado.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        mockMvc.perform(get("/api/v1/pedidos/9999")).andExpect(status().isNotFound());
    }
}
