package com.bookpoint.sucursal.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.sucursal.dto.SucursalDTO;
import com.bookpoint.sucursal.service.SucursalService;
import com.bookpoint.sucursal.model.Sucursal;
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

@WebMvcTest(SucursalController.class)
@ActiveProfiles("test")
public class SucursalControllerTest {
    @Autowired private MockMvc mockMvc;
    @SuppressWarnings("removal") @MockBean private SucursalService sucursalService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void testListarTodos() throws Exception {
        Sucursal obj = new Sucursal(1L, "BookPoint Concepcion", "Barros Arana 123", "Concepcion", "412345678", "9:00-18:00");
        Mockito.when(sucursalService.listarSucursals()).thenReturn(Arrays.asList(obj));
        mockMvc.perform(get("/api/v1/sucursales")).andExpect(status().isOk()).andExpect(jsonPath("$",hasSize(1)));
    }
    @Test
    void testCrear() throws Exception {
        SucursalDTO dto = new SucursalDTO(null, "BookPoint Concepcion", "Barros Arana 123", "Concepcion", "412345678", "9:00-18:00");
        Sucursal guardado = new Sucursal(1L, "BookPoint Concepcion", "Barros Arana 123", "Concepcion", "412345678", "9:00-18:00");
        Mockito.when(sucursalService.guardarSucursal(ArgumentMatchers.<Sucursal>any())).thenReturn(guardado);
        mockMvc.perform(post("/api/v1/sucursales").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    void testCrearConDatosInvalidosRetorna400() throws Exception {
        SucursalDTO dto = new SucursalDTO(null, "BookPoint Concepcion", "Barros Arana 123", "Concepcion", "412345678", "9:00-18:00");
        mockMvc.perform(post("/api/v1/sucursales").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
    @Test
    void testObtenerPorIdExistente() throws Exception {
        Sucursal obj = new Sucursal(1L, "BookPoint Concepcion", "Barros Arana 123", "Concepcion", "412345678", "9:00-18:00");
        Mockito.when(sucursalService.obtenerSucursalPorId(1L)).thenReturn(Optional.of(obj));
        mockMvc.perform(get("/api/v1/sucursales/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        Mockito.when(sucursalService.obtenerSucursalPorId(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/sucursales/99")).andExpect(status().isNotFound());
    }
    @Test
    void testActualizar() throws Exception {
        Sucursal actualizado = new Sucursal(1L, "BookPoint Concepcion", "Barros Arana 123", "Concepcion", "412345678", "9:00-18:00");
        Mockito.when(sucursalService.actualizarSucursal(eq(1L),ArgumentMatchers.<Sucursal>any())).thenReturn(actualizado);
        mockMvc.perform(put("/api/v1/sucursales/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SucursalDTO(1L, "BookPoint Concepcion", "Barros Arana 123", "Concepcion", "412345678", "9:00-18:00"))))
            .andExpect(status().isOk());
    }
    @Test
    void testActualizarNoExistente() throws Exception {
        SucursalDTO dto = new SucursalDTO(null, "BookPoint Concepcion", "Barros Arana 123", "Concepcion", "412345678", "9:00-18:00");
        Mockito.when(sucursalService.actualizarSucursal(eq(99L),ArgumentMatchers.<Sucursal>any()))
            .thenThrow(new RuntimeException("No existe"));
        mockMvc.perform(put("/api/v1/sucursales/99").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound());
    }
    @Test
    void testEliminar() throws Exception {
        Mockito.doNothing().when(sucursalService).eliminarSucursal(1L);
        mockMvc.perform(delete("/api/v1/sucursales/1")).andExpect(status().isNoContent());
    }
}
