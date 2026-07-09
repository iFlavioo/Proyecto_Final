package com.bookpoint.resena.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.resena.dto.ResenaDTO;
import com.bookpoint.resena.service.ResenaService;
import com.bookpoint.resena.model.Resena;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Optional;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResenaController.class)
@ActiveProfiles("test")
public class ResenaControllerTest {
    @Autowired private MockMvc mockMvc;
    @SuppressWarnings("removal") @MockBean private ResenaService resenaService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void testListarTodos() throws Exception {
        Resena obj = new Resena(1L, 1L, 1L, "Excelente libro, muy recomendado", 4, java.time.LocalDate.now());
        Mockito.when(resenaService.listarResenas()).thenReturn(Arrays.asList(obj));
        mockMvc.perform(get("/api/v1/resenas")).andExpect(status().isOk()).andExpect(jsonPath("$",hasSize(1)));
    }
    @Test
    void testCrear() throws Exception {
        ResenaDTO dto = new ResenaDTO(null, 1L, 1L, "Excelente libro, muy recomendado", 4, java.time.LocalDate.now());
        Resena guardado = new Resena(1L, 1L, 1L, "Excelente libro, muy recomendado", 4, java.time.LocalDate.now());
        Mockito.when(resenaService.guardarResena(ArgumentMatchers.<Resena>any())).thenReturn(guardado);
        mockMvc.perform(post("/api/v1/resenas").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    void testCrearConDatosInvalidosRetorna400() throws Exception {
        ResenaDTO dto = new ResenaDTO(null, 1L, 1L, "Excelente libro, muy recomendado", 4, java.time.LocalDate.now());
        mockMvc.perform(post("/api/v1/resenas").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
    @Test
    void testObtenerPorIdExistente() throws Exception {
        Resena obj = new Resena(1L, 1L, 1L, "Excelente libro, muy recomendado", 4, java.time.LocalDate.now());
        Mockito.when(resenaService.obtenerResenaPorId(1L)).thenReturn(Optional.of(obj));
        mockMvc.perform(get("/api/v1/resenas/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        Mockito.when(resenaService.obtenerResenaPorId(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/resenas/99")).andExpect(status().isNotFound());
    }
    @Test
    void testActualizar() throws Exception {
        Resena actualizado = new Resena(1L, 1L, 1L, "Excelente libro, muy recomendado", 4, java.time.LocalDate.now());
        Mockito.when(resenaService.actualizarResena(eq(1L),ArgumentMatchers.<Resena>any())).thenReturn(actualizado);
        mockMvc.perform(put("/api/v1/resenas/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ResenaDTO(1L, 1L, 1L, "Excelente libro, muy recomendado", 4, java.time.LocalDate.now()))))
            .andExpect(status().isOk());
    }
    @Test
    void testActualizarNoExistente() throws Exception {
        ResenaDTO dto = new ResenaDTO(null, 1L, 1L, "Excelente libro, muy recomendado", 4, java.time.LocalDate.now());
        Mockito.when(resenaService.actualizarResena(eq(99L),ArgumentMatchers.<Resena>any()))
            .thenThrow(new RuntimeException("No existe"));
        mockMvc.perform(put("/api/v1/resenas/99").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound());
    }
    @Test
    void testEliminar() throws Exception {
        Mockito.doNothing().when(resenaService).eliminarResena(1L);
        mockMvc.perform(delete("/api/v1/resenas/1")).andExpect(status().isNoContent());
    }
}
