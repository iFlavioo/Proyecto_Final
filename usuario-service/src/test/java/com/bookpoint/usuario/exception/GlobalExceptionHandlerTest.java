package com.bookpoint.usuario.exception;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Prueba directa del manejador global de excepciones.
 * Se testean ambos metodos explicitamente para asegurar 100% de cobertura,
 * ya que "manejarRuntime" no siempre se alcanza de forma natural via
 * los endpoints (algunos controladores capturan la excepcion antes).
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void manejarValidacionesRetorna400ConErroresDeCampo() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objeto", "campoX", "El campo es obligatorio");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(java.util.List.of(fieldError));

        ResponseEntity<Map<String, String>> resp = handler.manejarValidaciones(ex);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).containsEntry("campoX", "El campo es obligatorio");
    }

    @Test
    void manejarRuntimeRetorna400ConMensajeDeError() {
        RuntimeException ex = new RuntimeException("No existe el recurso con id: 999");

        ResponseEntity<Map<String, String>> resp = handler.manejarRuntime(ex);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).containsEntry("error", "No existe el recurso con id: 999");
    }
}
