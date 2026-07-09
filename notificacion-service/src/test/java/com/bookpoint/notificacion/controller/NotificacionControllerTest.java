package com.bookpoint.notificacion.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.notificacion.dto.NotificacionDTO;
import com.bookpoint.notificacion.service.NotificacionService;
import com.bookpoint.notificacion.model.Notificacion;
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

@WebMvcTest(NotificacionController.class)
@ActiveProfiles("test")
public class NotificacionControllerTest {
    @Autowired private MockMvc mockMvc;
    @SuppressWarnings("removal") @MockBean private NotificacionService notificacionService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void testListarTodos() throws Exception {
        Notificacion obj = new Notificacion(1L, 1L, "Tu pedido ha sido confirmado", "PEDIDO", false, java.time.LocalDate.now());
        Mockito.when(notificacionService.listarNotificacions()).thenReturn(Arrays.asList(obj));
        mockMvc.perform(get("/api/v1/notificaciones")).andExpect(status().isOk()).andExpect(jsonPath("$",hasSize(1)));
    }
    @Test
    void testCrear() throws Exception {
        NotificacionDTO dto = new NotificacionDTO(null, 1L, "Tu pedido ha sido confirmado", "PEDIDO", false, java.time.LocalDate.now());
        Notificacion guardado = new Notificacion(1L, 1L, "Tu pedido ha sido confirmado", "PEDIDO", false, java.time.LocalDate.now());
        Mockito.when(notificacionService.guardarNotificacion(ArgumentMatchers.<Notificacion>any())).thenReturn(guardado);
        mockMvc.perform(post("/api/v1/notificaciones").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    void testCrearConDatosInvalidosRetorna400() throws Exception {
        NotificacionDTO dto = new NotificacionDTO(null, 1L, "Tu pedido ha sido confirmado", "PEDIDO", false, java.time.LocalDate.now());
        mockMvc.perform(post("/api/v1/notificaciones").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
    @Test
    void testObtenerPorIdExistente() throws Exception {
        Notificacion obj = new Notificacion(1L, 1L, "Tu pedido ha sido confirmado", "PEDIDO", false, java.time.LocalDate.now());
        Mockito.when(notificacionService.obtenerNotificacionPorId(1L)).thenReturn(Optional.of(obj));
        mockMvc.perform(get("/api/v1/notificaciones/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        Mockito.when(notificacionService.obtenerNotificacionPorId(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/notificaciones/99")).andExpect(status().isNotFound());
    }
    @Test
    void testActualizar() throws Exception {
        Notificacion actualizado = new Notificacion(1L, 1L, "Tu pedido ha sido confirmado", "PEDIDO", false, java.time.LocalDate.now());
        Mockito.when(notificacionService.actualizarNotificacion(eq(1L),ArgumentMatchers.<Notificacion>any())).thenReturn(actualizado);
        mockMvc.perform(put("/api/v1/notificaciones/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new NotificacionDTO(1L, 1L, "Tu pedido ha sido confirmado", "PEDIDO", false, java.time.LocalDate.now()))))
            .andExpect(status().isOk());
    }
    @Test
    void testActualizarNoExistente() throws Exception {
        NotificacionDTO dto = new NotificacionDTO(null, 1L, "Tu pedido ha sido confirmado", "PEDIDO", false, java.time.LocalDate.now());
        Mockito.when(notificacionService.actualizarNotificacion(eq(99L),ArgumentMatchers.<Notificacion>any()))
            .thenThrow(new RuntimeException("No existe"));
        mockMvc.perform(put("/api/v1/notificaciones/99").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound());
    }
    @Test
    void testEliminar() throws Exception {
        Mockito.doNothing().when(notificacionService).eliminarNotificacion(1L);
        mockMvc.perform(delete("/api/v1/notificaciones/1")).andExpect(status().isNoContent());
    }
}
