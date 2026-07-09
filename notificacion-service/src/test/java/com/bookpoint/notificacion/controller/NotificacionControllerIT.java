package com.bookpoint.notificacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.notificacion.dto.NotificacionDTO;
import com.bookpoint.notificacion.model.Notificacion;
import com.bookpoint.notificacion.repository.NotificacionRepository;
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
class NotificacionControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private NotificacionRepository notificacionRepository;
    @Autowired private ObjectMapper objectMapper;

    @SuppressWarnings("removal")
    @MockBean private RestTemplate restTemplate;

    @BeforeEach
    void limpiar() {
        notificacionRepository.deleteAll();
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
            .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
    }

    @Test
    void testCrearYListar() throws Exception {
        NotificacionDTO dto = new NotificacionDTO(null, 1L, "Tu pedido ha sido confirmado", "PEDIDO", false, java.time.LocalDate.now());
        mockMvc.perform(post("/api/v1/notificaciones").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").exists());
        mockMvc.perform(get("/api/v1/notificaciones")).andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void testCrearConIdExternoInvalido() throws Exception {
        when(restTemplate.getForEntity(contains("/usuarios/"), eq(String.class)))
            .thenThrow(new RestClientException("404 Not Found"));
        NotificacionDTO dto = new NotificacionDTO(null, 1L, "Tu pedido ha sido confirmado", "PEDIDO", false, java.time.LocalDate.now());
        mockMvc.perform(post("/api/v1/notificaciones").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testCrearConDatosInvalidosRetorna400() throws Exception {
        mockMvc.perform(post("/api/v1/notificaciones").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testEliminar() throws Exception {
        Notificacion guardado = notificacionRepository.save(new Notificacion(null, 1L, "Tu pedido ha sido confirmado", "PEDIDO", false, java.time.LocalDate.now()));
        mockMvc.perform(delete("/api/v1/notificaciones/"+guardado.getId())).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/notificaciones/"+guardado.getId())).andExpect(status().isNotFound());
    }

    @Test
    void testActualizar() throws Exception {
        Notificacion guardado = notificacionRepository.save(new Notificacion(null, 1L, "Tu pedido ha sido confirmado", "PEDIDO", false, java.time.LocalDate.now()));
        NotificacionDTO dto = new NotificacionDTO(null, 1L, "Stock minimo alcanzado", "STOCK", true, java.time.LocalDate.now());
        mockMvc.perform(put("/api/v1/notificaciones/"+guardado.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        mockMvc.perform(get("/api/v1/notificaciones/9999")).andExpect(status().isNotFound());
    }
}
