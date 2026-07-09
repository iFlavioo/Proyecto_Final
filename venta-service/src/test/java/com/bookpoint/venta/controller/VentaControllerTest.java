package com.bookpoint.venta.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.venta.dto.VentaDTO;
import com.bookpoint.venta.dto.VentaDetalleDTO;
import com.bookpoint.venta.service.VentaService;
import com.bookpoint.venta.model.Venta;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VentaController.class)
@ActiveProfiles("test")
public class VentaControllerTest {
    @Autowired private MockMvc mockMvc;
    @SuppressWarnings("removal") @MockBean private VentaService ventaService;
    @Autowired private ObjectMapper objectMapper;

    private VentaDTO buildDTO(Long id) {
        VentaDetalleDTO det = new VentaDetalleDTO(null,1L,2,15990.0,31980.0);
        return new VentaDTO(id,1L,1L,LocalDate.now(),31980.0,List.of(det));
    }
    private Venta buildVenta(Long id) {
        Venta v = new Venta(); v.setId(id); v.setDetalles(new java.util.ArrayList<>()); return v;
    }

    @Test void testCrearVenta() throws Exception {
        VentaDTO req = buildDTO(null);
        Mockito.when(ventaService.guardarVenta(ArgumentMatchers.<Venta>any())).thenReturn(buildVenta(1L));
        mockMvc.perform(post("/api/v1/ventas").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }
    @Test void testListarTodas() throws Exception {
        Mockito.when(ventaService.listarVentas()).thenReturn(Arrays.asList(buildVenta(1L)));
        mockMvc.perform(get("/api/v1/ventas")).andExpect(status().isOk()).andExpect(jsonPath("$",hasSize(1)));
    }
    @Test void testObtenerPorIdExistente() throws Exception {
        Mockito.when(ventaService.obtenerVentaPorId(1L)).thenReturn(Optional.of(buildVenta(1L)));
        mockMvc.perform(get("/api/v1/ventas/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }
    @Test void testObtenerPorIdNoExistente() throws Exception {
        Mockito.when(ventaService.obtenerVentaPorId(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/ventas/99")).andExpect(status().isNotFound());
    }
    @Test void testActualizarVenta() throws Exception {
        Mockito.when(ventaService.actualizarVenta(eq(1L),ArgumentMatchers.<Venta>any())).thenReturn(buildVenta(1L));
        mockMvc.perform(put("/api/v1/ventas/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildDTO(1L))))
            .andExpect(status().isOk());
    }
    @Test void testActualizarNoExistente() throws Exception {
        Mockito.when(ventaService.actualizarVenta(eq(99L),ArgumentMatchers.<Venta>any()))
            .thenThrow(new RuntimeException("No existe"));
        mockMvc.perform(put("/api/v1/ventas/99").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildDTO(null))))
            .andExpect(status().isNotFound());
    }
    @Test void testEliminar() throws Exception {
        Mockito.doNothing().when(ventaService).eliminarVenta(1L);
        mockMvc.perform(delete("/api/v1/ventas/1")).andExpect(status().isNoContent());
    }
}
