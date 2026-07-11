package com.bookpoint.producto.service;

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

import com.bookpoint.producto.model.Producto;
import com.bookpoint.producto.repository.ProductoRepository;

public class ProductoServiceTest {
    @Mock private ProductoRepository productoRepository;
    @InjectMocks private ProductoService productoService;

    @BeforeEach void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void testGuardarProducto() {
        Producto nuevo = new Producto(null, "Cien Anios de Soledad", "Novela", 15990.0, "Literatura", "Garcia Marquez", "Sudamericana");
        Producto guardado = new Producto(1L, "Cien Anios de Soledad", "Novela", 15990.0, "Literatura", "Garcia Marquez", "Sudamericana");
        when(productoRepository.save(nuevo)).thenReturn(guardado);
        Producto resultado = productoService.guardarProducto(nuevo);
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(productoRepository).save(nuevo);
    }

    @Test
    void testListarProductos() {
        List<Producto> lista = Arrays.asList(new Producto(1L, "Cien Anios de Soledad", "Novela", 15990.0, "Literatura", "Garcia Marquez", "Sudamericana"));
        when(productoRepository.findAll()).thenReturn(lista);
        List<Producto> resultado = productoService.listarProductos();
        assertThat(resultado).hasSize(1);
        verify(productoRepository).findAll();
    }

    @Test
    void testObtenerProductoPorIdExistente() {
        Producto obj = new Producto(1L, "Cien Anios de Soledad", "Novela", 15990.0, "Literatura", "Garcia Marquez", "Sudamericana");
        when(productoRepository.findById(1L)).thenReturn(Optional.of(obj));
        Optional<Producto> resultado = productoService.obtenerProductoPorId(1L);
        assertThat(resultado).isPresent();
        verify(productoRepository).findById(1L);
    }

    @Test
    void testObtenerProductoPorIdNoExistente() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Producto> resultado = productoService.obtenerProductoPorId(99L);
        assertThat(resultado).isEmpty();
        verify(productoRepository).findById(99L);
    }

    @Test
    void testActualizarProducto() {
        Producto existente = new Producto(1L, "Cien Anios de Soledad", "Novela", 15990.0, "Literatura", "Garcia Marquez", "Sudamericana");
        Producto nuevo = new Producto(null, "El Principito", "Novela corta", 19990.0, "Infantil", "Saint-Exupery", "Salamandra");
        when(productoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));
        Producto resultado = productoService.actualizarProducto(1L, nuevo);
        assertThat(resultado).isNotNull();
        verify(productoRepository).save(existente);
    }

    @Test
    void testActualizarProductoNoExistente() {
        Producto nuevo = new Producto(null, "El Principito", "Novela corta", 19990.0, "Infantil", "Saint-Exupery", "Salamandra");
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> productoService.actualizarProducto(99L, nuevo));
    }

    @Test
    void testEliminarProducto() {
        doNothing().when(productoRepository).deleteById(1L);
        productoService.eliminarProducto(1L);
        verify(productoRepository).deleteById(1L);
    }

    // ─── Validacion: los datos no pueden estar nulos, vacios ni en blanco ──────

    @Test
    void testNoGuardaProductoConNombreNuloVacioOEnBlanco() {
        for (String invalido : new String[]{null, "", "   "}) {
            Producto p = new Producto(null, invalido, "Novela", 15990.0, "Literatura", "Garcia Marquez", "Sudamericana");
            assertThrows(IllegalArgumentException.class, () -> productoService.guardarProducto(p));
        }
        verify(productoRepository, never()).save(any());
    }

    @Test
    void testNoGuardaProductoConCategoriaNulaVaciaOEnBlanco() {
        for (String invalido : new String[]{null, "", "   "}) {
            Producto p = new Producto(null, "Cien Anios de Soledad", "Novela", 15990.0, invalido, "Garcia Marquez", "Sudamericana");
            assertThrows(IllegalArgumentException.class, () -> productoService.guardarProducto(p));
        }
        verify(productoRepository, never()).save(any());
    }
}
