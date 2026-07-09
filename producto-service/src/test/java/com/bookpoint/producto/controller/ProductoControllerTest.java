package com.bookpoint.producto.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.producto.dto.ProductoDTO;
import com.bookpoint.producto.service.ProductoService;
import com.bookpoint.producto.model.Producto;
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

@WebMvcTest(ProductoController.class)
@ActiveProfiles("test")
public class ProductoControllerTest {
    @Autowired private MockMvc mockMvc;
    @SuppressWarnings("removal") @MockBean private ProductoService productoService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void testListarTodos() throws Exception {
        Producto obj = new Producto(1L, "Cien Anios de Soledad", "Novela", 15990.0, "Literatura", "Garcia Marquez", "Sudamericana");
        Mockito.when(productoService.listarProductos()).thenReturn(Arrays.asList(obj));
        mockMvc.perform(get("/api/v1/productos")).andExpect(status().isOk()).andExpect(jsonPath("$",hasSize(1)));
    }
    @Test
    void testCrear() throws Exception {
        ProductoDTO dto = new ProductoDTO(null, "Cien Anios de Soledad", "Novela", 15990.0, "Literatura", "Garcia Marquez", "Sudamericana");
        Producto guardado = new Producto(1L, "Cien Anios de Soledad", "Novela", 15990.0, "Literatura", "Garcia Marquez", "Sudamericana");
        Mockito.when(productoService.guardarProducto(ArgumentMatchers.<Producto>any())).thenReturn(guardado);
        mockMvc.perform(post("/api/v1/productos").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    void testCrearConDatosInvalidosRetorna400() throws Exception {
        ProductoDTO dto = new ProductoDTO(null, "Cien Anios de Soledad", "Novela", 15990.0, "Literatura", "Garcia Marquez", "Sudamericana");
        mockMvc.perform(post("/api/v1/productos").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
    @Test
    void testObtenerPorIdExistente() throws Exception {
        Producto obj = new Producto(1L, "Cien Anios de Soledad", "Novela", 15990.0, "Literatura", "Garcia Marquez", "Sudamericana");
        Mockito.when(productoService.obtenerProductoPorId(1L)).thenReturn(Optional.of(obj));
        mockMvc.perform(get("/api/v1/productos/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        Mockito.when(productoService.obtenerProductoPorId(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/productos/99")).andExpect(status().isNotFound());
    }
    @Test
    void testActualizar() throws Exception {
        Producto actualizado = new Producto(1L, "Cien Anios de Soledad", "Novela", 15990.0, "Literatura", "Garcia Marquez", "Sudamericana");
        Mockito.when(productoService.actualizarProducto(eq(1L),ArgumentMatchers.<Producto>any())).thenReturn(actualizado);
        mockMvc.perform(put("/api/v1/productos/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ProductoDTO(1L, "Cien Anios de Soledad", "Novela", 15990.0, "Literatura", "Garcia Marquez", "Sudamericana"))))
            .andExpect(status().isOk());
    }
    @Test
    void testActualizarNoExistente() throws Exception {
        ProductoDTO dto = new ProductoDTO(null, "Cien Anios de Soledad", "Novela", 15990.0, "Literatura", "Garcia Marquez", "Sudamericana");
        Mockito.when(productoService.actualizarProducto(eq(99L),ArgumentMatchers.<Producto>any()))
            .thenThrow(new RuntimeException("No existe"));
        mockMvc.perform(put("/api/v1/productos/99").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound());
    }
    @Test
    void testEliminar() throws Exception {
        Mockito.doNothing().when(productoService).eliminarProducto(1L);
        mockMvc.perform(delete("/api/v1/productos/1")).andExpect(status().isNoContent());
    }
}
