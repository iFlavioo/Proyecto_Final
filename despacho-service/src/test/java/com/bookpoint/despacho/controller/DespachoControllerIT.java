package com.bookpoint.despacho.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.despacho.dto.DespachoDTO;
import com.bookpoint.despacho.model.Despacho;
import com.bookpoint.despacho.repository.DespachoRepository;
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
class DespachoControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private DespachoRepository despachoRepository;
    @Autowired private ObjectMapper objectMapper;

    @SuppressWarnings("removal")
    @MockBean private RestTemplate restTemplate;

    @BeforeEach
    void limpiar() {
        despachoRepository.deleteAll();
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
            .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
    }

    @Test
    void testCrearYListar() throws Exception {
        DespachoDTO dto = new DespachoDTO(null, 1L, 1L, "PREPARANDO", java.time.LocalDate.now(), "Av Siempre Viva 742");
        mockMvc.perform(post("/api/v1/despachos").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").exists());
        mockMvc.perform(get("/api/v1/despachos")).andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void testCrearConIdExternoInvalido() throws Exception {
        when(restTemplate.getForEntity(contains("/pedidos/"), eq(String.class)))
            .thenThrow(new RestClientException("404 Not Found"));
        DespachoDTO dto = new DespachoDTO(null, 1L, 1L, "PREPARANDO", java.time.LocalDate.now(), "Av Siempre Viva 742");
        mockMvc.perform(post("/api/v1/despachos").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testCrearConDatosInvalidosRetorna400() throws Exception {
        mockMvc.perform(post("/api/v1/despachos").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testEliminar() throws Exception {
        Despacho guardado = despachoRepository.save(new Despacho(null, 1L, 1L, "PREPARANDO", java.time.LocalDate.now(), "Av Siempre Viva 742"));
        mockMvc.perform(delete("/api/v1/despachos/"+guardado.getId())).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/despachos/"+guardado.getId())).andExpect(status().isNotFound());
    }

    @Test
    void testActualizar() throws Exception {
        Despacho guardado = despachoRepository.save(new Despacho(null, 1L, 1L, "PREPARANDO", java.time.LocalDate.now(), "Av Siempre Viva 742"));
        DespachoDTO dto = new DespachoDTO(null, 1L, 1L, "EN_CAMINO", java.time.LocalDate.now(), "Calle Falsa 123");
        mockMvc.perform(put("/api/v1/despachos/"+guardado.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        mockMvc.perform(get("/api/v1/despachos/9999")).andExpect(status().isNotFound());
    }
}
