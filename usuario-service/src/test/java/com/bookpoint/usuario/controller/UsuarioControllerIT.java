package com.bookpoint.usuario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.usuario.dto.UsuarioDTO;
import com.bookpoint.usuario.model.Usuario;
import com.bookpoint.usuario.repository.UsuarioRepository;
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
class UsuarioControllerIT {
    @Autowired private MockMvc mockMvc;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach void limpiar() { usuarioRepository.deleteAll(); }

    @Test
    void testCrearYListar() throws Exception {
        UsuarioDTO dto = new UsuarioDTO(null, "Juan Perez", "juan@bookpoint.cl", "pass1234", "CLIENTE");
        mockMvc.perform(post("/api/v1/usuarios").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").exists());
        mockMvc.perform(get("/api/v1/usuarios")).andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void testCrearConDatosInvalidosRetorna400() throws Exception {
        mockMvc.perform(post("/api/v1/usuarios").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testEliminar() throws Exception {
        Usuario guardado = usuarioRepository.save(new Usuario(null, "Juan Perez", "juan@bookpoint.cl", "pass1234", "CLIENTE"));
        mockMvc.perform(delete("/api/v1/usuarios/"+guardado.getId())).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/usuarios/"+guardado.getId())).andExpect(status().isNotFound());
    }

    @Test
    void testActualizar() throws Exception {
        Usuario guardado = usuarioRepository.save(new Usuario(null, "Juan Perez", "juan@bookpoint.cl", "pass1234", "CLIENTE"));
        UsuarioDTO dto = new UsuarioDTO(null, "Juan Actualizado", "juan.nuevo@bookpoint.cl", "clave5678", "ADMIN");
        mockMvc.perform(put("/api/v1/usuarios/"+guardado.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios/9999")).andExpect(status().isNotFound());
    }
}
