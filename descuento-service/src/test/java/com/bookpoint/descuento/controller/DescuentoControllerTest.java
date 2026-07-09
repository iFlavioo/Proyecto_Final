package com.bookpoint.descuento.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.descuento.dto.DescuentoDTO;
import com.bookpoint.descuento.service.DescuentoService;
import com.bookpoint.descuento.model.Descuento;
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

@WebMvcTest(DescuentoController.class)
@ActiveProfiles("test")
public class DescuentoControllerTest {
    @Autowired private MockMvc mockMvc;
    @SuppressWarnings("removal") @MockBean private DescuentoService descuentoService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void testListarTodos() throws Exception {
        Descuento obj = new Descuento(1L, "CUPON01", "Descuento verano", 10.0, "CUPON", true, java.time.LocalDate.now().plusDays(30));
        Mockito.when(descuentoService.listarDescuentos()).thenReturn(Arrays.asList(obj));
        mockMvc.perform(get("/api/v1/descuentos")).andExpect(status().isOk()).andExpect(jsonPath("$",hasSize(1)));
    }
    @Test
    void testCrear() throws Exception {
        DescuentoDTO dto = new DescuentoDTO(null, "CUPON01", "Descuento verano", 10.0, "CUPON", true, java.time.LocalDate.now().plusDays(30));
        Descuento guardado = new Descuento(1L, "CUPON01", "Descuento verano", 10.0, "CUPON", true, java.time.LocalDate.now().plusDays(30));
        Mockito.when(descuentoService.guardarDescuento(ArgumentMatchers.<Descuento>any())).thenReturn(guardado);
        mockMvc.perform(post("/api/v1/descuentos").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    void testCrearConDatosInvalidosRetorna400() throws Exception {
        DescuentoDTO dto = new DescuentoDTO(null, "CUPON01", "Descuento verano", 10.0, "CUPON", true, java.time.LocalDate.now().plusDays(30));
        mockMvc.perform(post("/api/v1/descuentos").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
    @Test
    void testObtenerPorIdExistente() throws Exception {
        Descuento obj = new Descuento(1L, "CUPON01", "Descuento verano", 10.0, "CUPON", true, java.time.LocalDate.now().plusDays(30));
        Mockito.when(descuentoService.obtenerDescuentoPorId(1L)).thenReturn(Optional.of(obj));
        mockMvc.perform(get("/api/v1/descuentos/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        Mockito.when(descuentoService.obtenerDescuentoPorId(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/descuentos/99")).andExpect(status().isNotFound());
    }
    @Test
    void testActualizar() throws Exception {
        Descuento actualizado = new Descuento(1L, "CUPON01", "Descuento verano", 10.0, "CUPON", true, java.time.LocalDate.now().plusDays(30));
        Mockito.when(descuentoService.actualizarDescuento(eq(1L),ArgumentMatchers.<Descuento>any())).thenReturn(actualizado);
        mockMvc.perform(put("/api/v1/descuentos/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new DescuentoDTO(1L, "CUPON01", "Descuento verano", 10.0, "CUPON", true, java.time.LocalDate.now().plusDays(30)))))
            .andExpect(status().isOk());
    }
    @Test
    void testActualizarNoExistente() throws Exception {
        DescuentoDTO dto = new DescuentoDTO(null, "CUPON01", "Descuento verano", 10.0, "CUPON", true, java.time.LocalDate.now().plusDays(30));
        Mockito.when(descuentoService.actualizarDescuento(eq(99L),ArgumentMatchers.<Descuento>any()))
            .thenThrow(new RuntimeException("No existe"));
        mockMvc.perform(put("/api/v1/descuentos/99").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound());
    }
    @Test
    void testEliminar() throws Exception {
        Mockito.doNothing().when(descuentoService).eliminarDescuento(1L);
        mockMvc.perform(delete("/api/v1/descuentos/1")).andExpect(status().isNoContent());
    }
}
