package com.bookpoint.sucursal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.sucursal.dto.SucursalDTO;
import com.bookpoint.sucursal.model.Sucursal;
import com.bookpoint.sucursal.repository.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class SucursalControllerIT {
    @Autowired private MockMvc mockMvc;
    @Autowired private SucursalRepository sucursalRepository;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach void limpiar() { sucursalRepository.deleteAll(); }

    @Test
    void testCrearYListar() throws Exception {
        SucursalDTO dto = new SucursalDTO(null, "BookPoint Concepcion", "Barros Arana 123", "Concepcion", "412345678", "9:00-18:00");
        mockMvc.perform(post("/api/v1/sucursales").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").exists());
        mockMvc.perform(get("/api/v1/sucursales")).andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void testCrearConDatosInvalidosRetorna400() throws Exception {
        mockMvc.perform(post("/api/v1/sucursales").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testEliminar() throws Exception {
        Sucursal guardado = sucursalRepository.save(new Sucursal(null, "BookPoint Concepcion", "Barros Arana 123", "Concepcion", "412345678", "9:00-18:00"));
        mockMvc.perform(delete("/api/v1/sucursales/"+guardado.getId())).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/sucursales/"+guardado.getId())).andExpect(status().isNotFound());
    }

    @Test
    void testActualizar() throws Exception {
        Sucursal guardado = sucursalRepository.save(new Sucursal(null, "BookPoint Concepcion", "Barros Arana 123", "Concepcion", "412345678", "9:00-18:00"));
        SucursalDTO dto = new SucursalDTO(null, "BookPoint Temuco", "Av Alemania 456", "Temuco", "452345678", "10:00-19:00");
        mockMvc.perform(put("/api/v1/sucursales/"+guardado.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        mockMvc.perform(get("/api/v1/sucursales/9999")).andExpect(status().isNotFound());
    }
}
