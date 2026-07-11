package com.bookpoint.resena.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.bookpoint.resena.model.Resena;
import com.bookpoint.resena.repository.ResenaRepository;

public class ResenaServiceTest {

    @Mock private ResenaRepository resenaRepository;
    @Mock private RestTemplate restTemplate;

    private ResenaService resenaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        resenaService = new ResenaService();
        ReflectionTestUtils.setField(resenaService, "resenaRepository", resenaRepository);
        ReflectionTestUtils.setField(resenaService, "restTemplate", restTemplate);
    }

    private void mockExisteTrue(String urlSubstring) {
        when(restTemplate.getForEntity(contains(urlSubstring), eq(String.class)))
            .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
    }

    private void mockExisteFalse(String urlSubstring) {
        when(restTemplate.getForEntity(contains(urlSubstring), eq(String.class)))
            .thenThrow(new RestClientException("404 Not Found"));
    }

    @Test
    void testGuardarResena() {
        Resena nuevo = new Resena(null, 1L, 1L, "Excelente libro, muy recomendado", 4, java.time.LocalDate.now());
        Resena guardado = new Resena(1L, 1L, 1L, "Excelente libro, muy recomendado", 4, java.time.LocalDate.now());
        mockExisteTrue("/usuarios/");
        mockExisteTrue("/productos/");
        when(resenaRepository.save(nuevo)).thenReturn(guardado);
        Resena resultado = resenaService.guardarResena(nuevo);
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(resenaRepository).save(nuevo);
    }

    @Test
    void testGuardarResenaConUsuarioInvalido() {
        Resena nuevo = new Resena(null, 1L, 1L, "Excelente libro, muy recomendado", 4, java.time.LocalDate.now());
        mockExisteTrue("/productos/");
        mockExisteFalse("/usuarios/");
        assertThrows(RuntimeException.class, () -> resenaService.guardarResena(nuevo));
        verify(resenaRepository, never()).save(any());
    }

    @Test
    void testGuardarResenaConProductoInvalido() {
        Resena nuevo = new Resena(null, 1L, 1L, "Excelente libro, muy recomendado", 4, java.time.LocalDate.now());
        mockExisteTrue("/usuarios/");
        mockExisteFalse("/productos/");
        assertThrows(RuntimeException.class, () -> resenaService.guardarResena(nuevo));
        verify(resenaRepository, never()).save(any());
    }

    @Test
    void testListarResenas() {
        List<Resena> lista = Arrays.asList(new Resena(1L, 1L, 1L, "Excelente libro, muy recomendado", 4, java.time.LocalDate.now()));
        when(resenaRepository.findAll()).thenReturn(lista);
        List<Resena> resultado = resenaService.listarResenas();
        assertThat(resultado).hasSize(1);
        verify(resenaRepository).findAll();
    }

    @Test
    void testObtenerResenaPorIdExistente() {
        Resena obj = new Resena(1L, 1L, 1L, "Excelente libro, muy recomendado", 4, java.time.LocalDate.now());
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(obj));
        Optional<Resena> resultado = resenaService.obtenerResenaPorId(1L);
        assertThat(resultado).isPresent();
        verify(resenaRepository).findById(1L);
    }

    @Test
    void testObtenerResenaPorIdNoExistente() {
        when(resenaRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Resena> resultado = resenaService.obtenerResenaPorId(99L);
        assertThat(resultado).isEmpty();
        verify(resenaRepository).findById(99L);
    }

    @Test
    void testActualizarResena() {
        Resena existente = new Resena(1L, 1L, 1L, "Excelente libro, muy recomendado", 4, java.time.LocalDate.now());
        Resena nuevo = new Resena(null, 1L, 1L, "Actualizo mi opinion: gran libro", 5, java.time.LocalDate.now());
        mockExisteTrue("/usuarios/");
        mockExisteTrue("/productos/");
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(resenaRepository.save(any(Resena.class))).thenAnswer(i -> i.getArgument(0));
        Resena resultado = resenaService.actualizarResena(1L, nuevo);
        assertThat(resultado).isNotNull();
        verify(resenaRepository).save(existente);
    }

    @Test
    void testActualizarResenaNoExistente() {
        Resena nuevo = new Resena(null, 1L, 1L, "Actualizo mi opinion: gran libro", 5, java.time.LocalDate.now());
        when(resenaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> resenaService.actualizarResena(99L, nuevo));
    }

    @Test
    void testEliminarResena() {
        doNothing().when(resenaRepository).deleteById(1L);
        resenaService.eliminarResena(1L);
        verify(resenaRepository).deleteById(1L);
    }

    // ─── Validacion: los datos no pueden estar nulos, vacios ni en blanco ──────

    @Test
    void testNoGuardaResenaConComentarioNuloVacioOEnBlanco() {
        for (String invalido : new String[]{null, "", "   "}) {
            Resena r = new Resena(null, 1L, 1L, invalido, 4, java.time.LocalDate.now());
            assertThrows(IllegalArgumentException.class, () -> resenaService.guardarResena(r));
        }
        verify(resenaRepository, never()).save(any());
    }
}
