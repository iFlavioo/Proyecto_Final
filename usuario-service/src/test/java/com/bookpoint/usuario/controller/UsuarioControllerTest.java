package com.bookpoint.usuario.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookpoint.usuario.dto.UsuarioDTO;
import com.bookpoint.usuario.service.UsuarioService;
import com.bookpoint.usuario.model.Usuario;
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

@WebMvcTest(UsuarioController.class)
@ActiveProfiles("test")
public class UsuarioControllerTest {
    @Autowired private MockMvc mockMvc;
    @SuppressWarnings("removal") @MockBean private UsuarioService usuarioService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void testListarTodos() throws Exception {
        Usuario obj = new Usuario(1L, "Juan Perez", "juan@bookpoint.cl", "pass1234", "CLIENTE");
        Mockito.when(usuarioService.listarUsuarios()).thenReturn(Arrays.asList(obj));
        mockMvc.perform(get("/api/v1/usuarios")).andExpect(status().isOk()).andExpect(jsonPath("$",hasSize(1)));
    }
    @Test
    void testCrear() throws Exception {
        UsuarioDTO dto = new UsuarioDTO(null, "Juan Perez", "juan@bookpoint.cl", "pass1234", "CLIENTE");
        Usuario guardado = new Usuario(1L, "Juan Perez", "juan@bookpoint.cl", "pass1234", "CLIENTE");
        Mockito.when(usuarioService.guardarUsuario(ArgumentMatchers.<Usuario>any())).thenReturn(guardado);
        mockMvc.perform(post("/api/v1/usuarios").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    void testCrearConDatosInvalidosRetorna400() throws Exception {
        UsuarioDTO dto = new UsuarioDTO(null, "Juan Perez", "juan@bookpoint.cl", "pass1234", "CLIENTE");
        mockMvc.perform(post("/api/v1/usuarios").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
    @Test
    void testObtenerPorIdExistente() throws Exception {
        Usuario obj = new Usuario(1L, "Juan Perez", "juan@bookpoint.cl", "pass1234", "CLIENTE");
        Mockito.when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(Optional.of(obj));
        mockMvc.perform(get("/api/v1/usuarios/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        Mockito.when(usuarioService.obtenerUsuarioPorId(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/usuarios/99")).andExpect(status().isNotFound());
    }
    @Test
    void testActualizar() throws Exception {
        Usuario actualizado = new Usuario(1L, "Juan Perez", "juan@bookpoint.cl", "pass1234", "CLIENTE");
        Mockito.when(usuarioService.actualizarUsuario(eq(1L),ArgumentMatchers.<Usuario>any())).thenReturn(actualizado);
        mockMvc.perform(put("/api/v1/usuarios/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UsuarioDTO(1L, "Juan Perez", "juan@bookpoint.cl", "pass1234", "CLIENTE"))))
            .andExpect(status().isOk());
    }
    @Test
    void testActualizarNoExistente() throws Exception {
        UsuarioDTO dto = new UsuarioDTO(null, "Juan Perez", "juan@bookpoint.cl", "pass1234", "CLIENTE");
        Mockito.when(usuarioService.actualizarUsuario(eq(99L),ArgumentMatchers.<Usuario>any()))
            .thenThrow(new RuntimeException("No existe"));
        mockMvc.perform(put("/api/v1/usuarios/99").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound());
    }
    @Test
    void testEliminar() throws Exception {
        Mockito.doNothing().when(usuarioService).eliminarUsuario(1L);
        mockMvc.perform(delete("/api/v1/usuarios/1")).andExpect(status().isNoContent());
    }
}
