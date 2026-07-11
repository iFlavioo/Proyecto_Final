package com.bookpoint.pedido.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO que representa la informacion de un Despacho dentro del contexto de pedido-service.
 * Se usa para enriquecer la respuesta de un pedido con el estado del envio asociado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DespachoDTO {

    private Long id;

    @NotNull(message = "El pedidoId no puede ser nulo ni vacio")
    @Positive(message = "El pedidoId debe ser mayor a cero")
    private Long pedidoId;

    @NotNull(message = "El sucursalId no puede ser nulo ni vacio")
    @Positive(message = "El sucursalId debe ser mayor a cero")
    private Long sucursalId;

    @NotBlank(message = "El estado no puede ser nulo ni vacio")
    private String estado;

    @NotNull(message = "La fecha de despacho no puede ser nula ni vacia")
    private LocalDate fechaDespacho;

    @NotBlank(message = "La direccion de destino no puede ser nula ni vacia")
    private String direccionDestino;
}
