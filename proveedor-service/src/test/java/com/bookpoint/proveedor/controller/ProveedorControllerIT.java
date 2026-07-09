package com.bookpoint.proveedor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.proveedor.dto.ProveedorDTO;
import com.bookpoint.proveedor.model.Proveedor;
import com.bookpoint.proveedor.repository.ProveedorRepository;
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
class ProveedorControllerIT {
    @Autowired private MockMvc mockMvc;
    @Autowired private ProveedorRepository proveedorRepository;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach void limpiar() { proveedorRepository.deleteAll(); }

    @Test
    void testCrearYListar() throws Exception {
        ProveedorDTO dto = new ProveedorDTO(null, "Editorial Planeta", "contacto@planeta.cl", "223456789", "Planeta", true);
        mockMvc.perform(post("/api/v1/proveedores").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").exists());
        mockMvc.perform(get("/api/v1/proveedores")).andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void testCrearConDatosInvalidosRetorna400() throws Exception {
        mockMvc.perform(post("/api/v1/proveedores").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testEliminar() throws Exception {
        Proveedor guardado = proveedorRepository.save(new Proveedor(null, "Editorial Planeta", "contacto@planeta.cl", "223456789", "Planeta", true));
        mockMvc.perform(delete("/api/v1/proveedores/"+guardado.getId())).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/proveedores/"+guardado.getId())).andExpect(status().isNotFound());
    }

    @Test
    void testActualizar() throws Exception {
        Proveedor guardado = proveedorRepository.save(new Proveedor(null, "Editorial Planeta", "contacto@planeta.cl", "223456789", "Planeta", true));
        ProveedorDTO dto = new ProveedorDTO(null, "Editorial Penguin", "ventas@penguin.cl", "224567890", "Penguin", false);
        mockMvc.perform(put("/api/v1/proveedores/"+guardado.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        mockMvc.perform(get("/api/v1/proveedores/9999")).andExpect(status().isNotFound());
    }
}
