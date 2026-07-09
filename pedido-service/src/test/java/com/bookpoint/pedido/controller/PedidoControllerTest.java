package com.bookpoint.pedido.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.pedido.dto.PedidoDTO;
import com.bookpoint.pedido.service.PedidoService;
import com.bookpoint.pedido.model.Pedido;
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

@WebMvcTest(PedidoController.class)
@ActiveProfiles("test")
public class PedidoControllerTest {
    @Autowired private MockMvc mockMvc;
    @SuppressWarnings("removal") @MockBean private PedidoService pedidoService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void testListarTodos() throws Exception {
        Pedido obj = new Pedido(1L, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742");
        Mockito.when(pedidoService.listarPedidos()).thenReturn(Arrays.asList(obj));
        mockMvc.perform(get("/api/v1/pedidos")).andExpect(status().isOk()).andExpect(jsonPath("$",hasSize(1)));
    }
    @Test
    void testCrear() throws Exception {
        PedidoDTO dto = new PedidoDTO(null, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742");
        Pedido guardado = new Pedido(1L, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742");
        Mockito.when(pedidoService.guardarPedido(ArgumentMatchers.<Pedido>any())).thenReturn(guardado);
        mockMvc.perform(post("/api/v1/pedidos").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    void testCrearConDatosInvalidosRetorna400() throws Exception {
        PedidoDTO dto = new PedidoDTO(null, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742");
        mockMvc.perform(post("/api/v1/pedidos").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
    @Test
    void testObtenerPorIdExistente() throws Exception {
        Pedido obj = new Pedido(1L, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742");
        Mockito.when(pedidoService.obtenerPedidoPorId(1L)).thenReturn(Optional.of(obj));
        mockMvc.perform(get("/api/v1/pedidos/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        Mockito.when(pedidoService.obtenerPedidoPorId(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/pedidos/99")).andExpect(status().isNotFound());
    }
    @Test
    void testActualizar() throws Exception {
        Pedido actualizado = new Pedido(1L, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742");
        Mockito.when(pedidoService.actualizarPedido(eq(1L),ArgumentMatchers.<Pedido>any())).thenReturn(actualizado);
        mockMvc.perform(put("/api/v1/pedidos/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PedidoDTO(1L, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742"))))
            .andExpect(status().isOk());
    }
    @Test
    void testActualizarNoExistente() throws Exception {
        PedidoDTO dto = new PedidoDTO(null, 1L, 1L, 2, "PENDIENTE", java.time.LocalDate.now(), "Av Siempre Viva 742");
        Mockito.when(pedidoService.actualizarPedido(eq(99L),ArgumentMatchers.<Pedido>any()))
            .thenThrow(new RuntimeException("No existe"));
        mockMvc.perform(put("/api/v1/pedidos/99").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound());
    }
    @Test
    void testEliminar() throws Exception {
        Mockito.doNothing().when(pedidoService).eliminarPedido(1L);
        mockMvc.perform(delete("/api/v1/pedidos/1")).andExpect(status().isNoContent());
    }
}
