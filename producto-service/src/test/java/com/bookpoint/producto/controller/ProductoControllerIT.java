package com.bookpoint.producto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.producto.dto.ProductoDTO;
import com.bookpoint.producto.model.Producto;
import com.bookpoint.producto.repository.ProductoRepository;
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
class ProductoControllerIT {
    @Autowired private MockMvc mockMvc;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach void limpiar() { productoRepository.deleteAll(); }

    @Test
    void testCrearYListar() throws Exception {
        ProductoDTO dto = new ProductoDTO(null, "Cien Anios de Soledad", "Novela", 15990.0, "Literatura", "Garcia Marquez", "Sudamericana");
        mockMvc.perform(post("/api/v1/productos").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").exists());
        mockMvc.perform(get("/api/v1/productos")).andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void testCrearConDatosInvalidosRetorna400() throws Exception {
        mockMvc.perform(post("/api/v1/productos").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testEliminar() throws Exception {
        Producto guardado = productoRepository.save(new Producto(null, "Cien Anios de Soledad", "Novela", 15990.0, "Literatura", "Garcia Marquez", "Sudamericana"));
        mockMvc.perform(delete("/api/v1/productos/"+guardado.getId())).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/productos/"+guardado.getId())).andExpect(status().isNotFound());
    }

    @Test
    void testActualizar() throws Exception {
        Producto guardado = productoRepository.save(new Producto(null, "Cien Anios de Soledad", "Novela", 15990.0, "Literatura", "Garcia Marquez", "Sudamericana"));
        ProductoDTO dto = new ProductoDTO(null, "El Principito", "Novela corta", 19990.0, "Infantil", "Saint-Exupery", "Salamandra");
        mockMvc.perform(put("/api/v1/productos/"+guardado.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        mockMvc.perform(get("/api/v1/productos/9999")).andExpect(status().isNotFound());
    }
}
