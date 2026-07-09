package com.bookpoint.devolucion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.devolucion.dto.DevolucionDTO;
import com.bookpoint.devolucion.model.Devolucion;
import com.bookpoint.devolucion.repository.DevolucionRepository;
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
class DevolucionControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private DevolucionRepository devolucionRepository;
    @Autowired private ObjectMapper objectMapper;

    @SuppressWarnings("removal")
    @MockBean private RestTemplate restTemplate;

    @BeforeEach
    void limpiar() {
        devolucionRepository.deleteAll();
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
            .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
    }

    @Test
    void testCrearYListar() throws Exception {
        DevolucionDTO dto = new DevolucionDTO(null, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now());
        mockMvc.perform(post("/api/v1/devoluciones").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").exists());
        mockMvc.perform(get("/api/v1/devoluciones")).andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void testCrearConIdExternoInvalido() throws Exception {
        when(restTemplate.getForEntity(contains("/usuarios/"), eq(String.class)))
            .thenThrow(new RestClientException("404 Not Found"));
        DevolucionDTO dto = new DevolucionDTO(null, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now());
        mockMvc.perform(post("/api/v1/devoluciones").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testCrearConDatosInvalidosRetorna400() throws Exception {
        mockMvc.perform(post("/api/v1/devoluciones").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testEliminar() throws Exception {
        Devolucion guardado = devolucionRepository.save(new Devolucion(null, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now()));
        mockMvc.perform(delete("/api/v1/devoluciones/"+guardado.getId())).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/devoluciones/"+guardado.getId())).andExpect(status().isNotFound());
    }

    @Test
    void testActualizar() throws Exception {
        Devolucion guardado = devolucionRepository.save(new Devolucion(null, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now()));
        DevolucionDTO dto = new DevolucionDTO(null, 1L, 1L, "Revisado y aprobado por tienda", "APROBADA", java.time.LocalDate.now());
        mockMvc.perform(put("/api/v1/devoluciones/"+guardado.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        mockMvc.perform(get("/api/v1/devoluciones/9999")).andExpect(status().isNotFound());
    }
}
