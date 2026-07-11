package com.bookpoint.pedido.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la informacion de un Usuario dentro del contexto de pedido-service.
 * Se usa para enriquecer la respuesta de un pedido con los datos del cliente que lo realizo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "El nombre del usuario no puede ser nulo ni vacio")
    private String nombre;

    @NotBlank(message = "El email no puede ser nulo ni vacio")
    @Email(message = "El email no tiene un formato valido")
    private String email;

    @NotBlank(message = "El rol no puede ser nulo ni vacio")
    private String rol;
}
