package com.bookpoint.descuento.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.descuento.dto.DescuentoDTO;
import com.bookpoint.descuento.model.Descuento;
import com.bookpoint.descuento.repository.DescuentoRepository;
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
class DescuentoControllerIT {
    @Autowired private MockMvc mockMvc;
    @Autowired private DescuentoRepository descuentoRepository;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach void limpiar() { descuentoRepository.deleteAll(); }

    @Test
    void testCrearYListar() throws Exception {
        DescuentoDTO dto = new DescuentoDTO(null, "CUPON01", "Descuento verano", 10.0, "CUPON", true, java.time.LocalDate.now().plusDays(30));
        mockMvc.perform(post("/api/v1/descuentos").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").exists());
        mockMvc.perform(get("/api/v1/descuentos")).andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void testCrearConDatosInvalidosRetorna400() throws Exception {
        mockMvc.perform(post("/api/v1/descuentos").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testEliminar() throws Exception {
        Descuento guardado = descuentoRepository.save(new Descuento(null, "CUPON01", "Descuento verano", 10.0, "CUPON", true, java.time.LocalDate.now().plusDays(30)));
        mockMvc.perform(delete("/api/v1/descuentos/"+guardado.getId())).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/descuentos/"+guardado.getId())).andExpect(status().isNotFound());
    }

    @Test
    void testActualizar() throws Exception {
        Descuento guardado = descuentoRepository.save(new Descuento(null, "CUPON01", "Descuento verano", 10.0, "CUPON", true, java.time.LocalDate.now().plusDays(30)));
        DescuentoDTO dto = new DescuentoDTO(null, "CUPON02", "Descuento invierno", 20.0, "PROMOCION", false, java.time.LocalDate.now().plusDays(60));
        mockMvc.perform(put("/api/v1/descuentos/"+guardado.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        mockMvc.perform(get("/api/v1/descuentos/9999")).andExpect(status().isNotFound());
    }
}
