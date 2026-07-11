package com.bookpoint.proveedor.service;

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

import com.bookpoint.proveedor.model.Proveedor;
import com.bookpoint.proveedor.repository.ProveedorRepository;

public class ProveedorServiceTest {
    @Mock private ProveedorRepository proveedorRepository;
    @InjectMocks private ProveedorService proveedorService;

    @BeforeEach void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void testGuardarProveedor() {
        Proveedor nuevo = new Proveedor(null, "Editorial Planeta", "contacto@planeta.cl", "223456789", "Planeta", true);
        Proveedor guardado = new Proveedor(1L, "Editorial Planeta", "contacto@planeta.cl", "223456789", "Planeta", true);
        when(proveedorRepository.save(nuevo)).thenReturn(guardado);
        Proveedor resultado = proveedorService.guardarProveedor(nuevo);
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(proveedorRepository).save(nuevo);
    }

    @Test
    void testListarProveedors() {
        List<Proveedor> lista = Arrays.asList(new Proveedor(1L, "Editorial Planeta", "contacto@planeta.cl", "223456789", "Planeta", true));
        when(proveedorRepository.findAll()).thenReturn(lista);
        List<Proveedor> resultado = proveedorService.listarProveedors();
        assertThat(resultado).hasSize(1);
        verify(proveedorRepository).findAll();
    }

    @Test
    void testObtenerProveedorPorIdExistente() {
        Proveedor obj = new Proveedor(1L, "Editorial Planeta", "contacto@planeta.cl", "223456789", "Planeta", true);
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(obj));
        Optional<Proveedor> resultado = proveedorService.obtenerProveedorPorId(1L);
        assertThat(resultado).isPresent();
        verify(proveedorRepository).findById(1L);
    }

    @Test
    void testObtenerProveedorPorIdNoExistente() {
        when(proveedorRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Proveedor> resultado = proveedorService.obtenerProveedorPorId(99L);
        assertThat(resultado).isEmpty();
        verify(proveedorRepository).findById(99L);
    }

    @Test
    void testActualizarProveedor() {
        Proveedor existente = new Proveedor(1L, "Editorial Planeta", "contacto@planeta.cl", "223456789", "Planeta", true);
        Proveedor nuevo = new Proveedor(null, "Editorial Penguin", "ventas@penguin.cl", "224567890", "Penguin", false);
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(proveedorRepository.save(any(Proveedor.class))).thenAnswer(i -> i.getArgument(0));
        Proveedor resultado = proveedorService.actualizarProveedor(1L, nuevo);
        assertThat(resultado).isNotNull();
        verify(proveedorRepository).save(existente);
    }

    @Test
    void testActualizarProveedorNoExistente() {
        Proveedor nuevo = new Proveedor(null, "Editorial Penguin", "ventas@penguin.cl", "224567890", "Penguin", false);
        when(proveedorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> proveedorService.actualizarProveedor(99L, nuevo));
    }

    @Test
    void testEliminarProveedor() {
        doNothing().when(proveedorRepository).deleteById(1L);
        proveedorService.eliminarProveedor(1L);
        verify(proveedorRepository).deleteById(1L);
    }

    // ─── Validacion: los datos no pueden estar nulos, vacios ni en blanco ──────

    @Test
    void testNoGuardaProveedorConNombreNuloVacioOEnBlanco() {
        for (String invalido : new String[]{null, "", "   "}) {
            Proveedor p = new Proveedor(null, invalido, "contacto@planeta.cl", "223456789", "Planeta", true);
            assertThrows(IllegalArgumentException.class, () -> proveedorService.guardarProveedor(p));
        }
        verify(proveedorRepository, never()).save(any());
    }

    @Test
    void testNoGuardaProveedorConEmailSinArroba() {
        Proveedor p = new Proveedor(null, "Editorial Planeta", "contactoplaneta.cl", "223456789", "Planeta", true);
        assertThrows(IllegalArgumentException.class, () -> proveedorService.guardarProveedor(p));
        verify(proveedorRepository, never()).save(any());
    }
}
