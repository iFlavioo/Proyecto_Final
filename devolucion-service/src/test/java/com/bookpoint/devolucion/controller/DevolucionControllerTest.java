package com.bookpoint.devolucion.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.devolucion.dto.DevolucionDTO;
import com.bookpoint.devolucion.service.DevolucionService;
import com.bookpoint.devolucion.model.Devolucion;
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

@WebMvcTest(DevolucionController.class)
@ActiveProfiles("test")
public class DevolucionControllerTest {
    @Autowired private MockMvc mockMvc;
    @SuppressWarnings("removal") @MockBean private DevolucionService devolucionService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void testListarTodos() throws Exception {
        Devolucion obj = new Devolucion(1L, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now());
        Mockito.when(devolucionService.listarDevolucions()).thenReturn(Arrays.asList(obj));
        mockMvc.perform(get("/api/v1/devoluciones")).andExpect(status().isOk()).andExpect(jsonPath("$",hasSize(1)));
    }
    @Test
    void testCrear() throws Exception {
        DevolucionDTO dto = new DevolucionDTO(null, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now());
        Devolucion guardado = new Devolucion(1L, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now());
        Mockito.when(devolucionService.guardarDevolucion(ArgumentMatchers.<Devolucion>any())).thenReturn(guardado);
        mockMvc.perform(post("/api/v1/devoluciones").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    void testCrearConDatosInvalidosRetorna400() throws Exception {
        DevolucionDTO dto = new DevolucionDTO(null, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now());
        mockMvc.perform(post("/api/v1/devoluciones").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
    @Test
    void testObtenerPorIdExistente() throws Exception {
        Devolucion obj = new Devolucion(1L, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now());
        Mockito.when(devolucionService.obtenerDevolucionPorId(1L)).thenReturn(Optional.of(obj));
        mockMvc.perform(get("/api/v1/devoluciones/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        Mockito.when(devolucionService.obtenerDevolucionPorId(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/devoluciones/99")).andExpect(status().isNotFound());
    }
    @Test
    void testActualizar() throws Exception {
        Devolucion actualizado = new Devolucion(1L, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now());
        Mockito.when(devolucionService.actualizarDevolucion(eq(1L),ArgumentMatchers.<Devolucion>any())).thenReturn(actualizado);
        mockMvc.perform(put("/api/v1/devoluciones/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new DevolucionDTO(1L, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now()))))
            .andExpect(status().isOk());
    }
    @Test
    void testActualizarNoExistente() throws Exception {
        DevolucionDTO dto = new DevolucionDTO(null, 1L, 1L, "Producto llego defectuoso", "PENDIENTE", java.time.LocalDate.now());
        Mockito.when(devolucionService.actualizarDevolucion(eq(99L),ArgumentMatchers.<Devolucion>any()))
            .thenThrow(new RuntimeException("No existe"));
        mockMvc.perform(put("/api/v1/devoluciones/99").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound());
    }
    @Test
    void testEliminar() throws Exception {
        Mockito.doNothing().when(devolucionService).eliminarDevolucion(1L);
        mockMvc.perform(delete("/api/v1/devoluciones/1")).andExpect(status().isNoContent());
    }
}
