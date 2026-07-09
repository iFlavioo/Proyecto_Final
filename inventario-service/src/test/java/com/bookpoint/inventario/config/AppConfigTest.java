package com.bookpoint.inventario.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prueba directa del bean de configuracion (sin levantar contexto de Spring).
 * Esto asegura que la linea "return new RestTemplate();" quede cubierta,
 * ya que en las pruebas de Service/Controller el RestTemplate se mockea
 * y el bean real nunca llega a instanciarse.
 */
class AppConfigTest {

    @Test
    void restTemplateBeanSeCreaCorrectamente() {
        AppConfig config = new AppConfig();
        RestTemplate resultado = config.restTemplate();
        assertThat(resultado).isNotNull();
        assertThat(resultado).isInstanceOf(RestTemplate.class);
    }
}
