package com.bookpoint.usuario.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bookpoint.usuario.model.Usuario;
import com.bookpoint.usuario.repository.UsuarioRepository;

public class UsuarioServiceTest {
    @Mock private UsuarioRepository usuarioRepository;
    @InjectMocks private UsuarioService usuarioService;

    @BeforeEach void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void testGuardarUsuario() {
        Usuario nuevo = new Usuario(null, "Juan Perez", "juan@bookpoint.cl", "pass1234", "CLIENTE");
        Usuario guardado = new Usuario(1L, "Juan Perez", "juan@bookpoint.cl", "pass1234", "CLIENTE");
        when(usuarioRepository.save(nuevo)).thenReturn(guardado);
        Usuario resultado = usuarioService.guardarUsuario(nuevo);
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(usuarioRepository).save(nuevo);
    }

    @Test
    void testListarUsuarios() {
        List<Usuario> lista = Arrays.asList(new Usuario(1L, "Juan Perez", "juan@bookpoint.cl", "pass1234", "CLIENTE"));
        when(usuarioRepository.findAll()).thenReturn(lista);
        List<Usuario> resultado = usuarioService.listarUsuarios();
        assertThat(resultado).hasSize(1);
        verify(usuarioRepository).findAll();
    }

    @Test
    void testObtenerUsuarioPorIdExistente() {
        Usuario obj = new Usuario(1L, "Juan Perez", "juan@bookpoint.cl", "pass1234", "CLIENTE");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(obj));
        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorId(1L);
        assertThat(resultado).isPresent();
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void testObtenerUsuarioPorIdNoExistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorId(99L);
        assertThat(resultado).isEmpty();
        verify(usuarioRepository).findById(99L);
    }

    @Test
    void testActualizarUsuario() {
        Usuario existente = new Usuario(1L, "Juan Perez", "juan@bookpoint.cl", "pass1234", "CLIENTE");
        Usuario nuevo = new Usuario(null, "Juan Actualizado", "juan.nuevo@bookpoint.cl", "clave5678", "ADMIN");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));
        Usuario resultado = usuarioService.actualizarUsuario(1L, nuevo);
        assertThat(resultado).isNotNull();
        verify(usuarioRepository).save(existente);
    }

    @Test
    void testActualizarUsuarioNoExistente() {
        Usuario nuevo = new Usuario(null, "Juan Actualizado", "juan.nuevo@bookpoint.cl", "clave5678", "ADMIN");
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> usuarioService.actualizarUsuario(99L, nuevo));
    }

    @Test
    void testEliminarUsuario() {
        doNothing().when(usuarioRepository).deleteById(1L);
        usuarioService.eliminarUsuario(1L);
        verify(usuarioRepository).deleteById(1L);
    }

    // ─── Validacion: los datos no pueden estar nulos, vacios ni en blanco ──────

    @Test
    void testNoGuardaUsuarioConNombreNuloVacioOEnBlanco() {
        for (String invalido : new String[]{null, "", "   "}) {
            Usuario u = new Usuario(null, invalido, "juan@bookpoint.cl", "pass1234", "CLIENTE");
            assertThrows(IllegalArgumentException.class, () -> usuarioService.guardarUsuario(u));
        }
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testNoGuardaUsuarioConEmailNuloVacioOEnBlanco() {
        for (String invalido : new String[]{null, "", "   "}) {
            Usuario u = new Usuario(null, "Juan Perez", invalido, "pass1234", "CLIENTE");
            assertThrows(IllegalArgumentException.class, () -> usuarioService.guardarUsuario(u));
        }
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testNoGuardaUsuarioConEmailSinArroba() {
        Usuario u = new Usuario(null, "Juan Perez", "juanbookpoint.cl", "pass1234", "CLIENTE");
        assertThrows(IllegalArgumentException.class, () -> usuarioService.guardarUsuario(u));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testNoGuardaUsuarioConPasswordNuloVacioOEnBlanco() {
        for (String invalido : new String[]{null, "", "   "}) {
            Usuario u = new Usuario(null, "Juan Perez", "juan@bookpoint.cl", invalido, "CLIENTE");
            assertThrows(IllegalArgumentException.class, () -> usuarioService.guardarUsuario(u));
        }
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testNoGuardaUsuarioConRolInvalido() {
        Usuario u = new Usuario(null, "Juan Perez", "juan@bookpoint.cl", "pass1234", "SUPERADMIN");
        assertThrows(IllegalArgumentException.class, () -> usuarioService.guardarUsuario(u));
        verify(usuarioRepository, never()).save(any());
    }
}
