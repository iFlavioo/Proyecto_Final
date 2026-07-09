package com.bookpoint.usuario;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Cubre la linea main() que @SpringBootTest no ejecuta directamente
 * (las pruebas de integracion bootean el contexto por otro camino).
 * Se usa el perfil "test" (H2 en memoria) y puerto aleatorio para
 * evitar conflictos con los servicios reales corriendo en otros puertos.
 */
class UsuarioServiceApplicationTests {

    @Test
    void mainEjecutaSinErrores() {
        assertDoesNotThrow(() -> {
            UsuarioServiceApplication.main(new String[] {
                "--spring.profiles.active=test",
                "--server.port=0"
            });
        });
    }
}
