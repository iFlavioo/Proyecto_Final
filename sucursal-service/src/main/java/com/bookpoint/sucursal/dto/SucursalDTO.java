package com.bookpoint.sucursal.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import com.bookpoint.sucursal.model.Sucursal;

@Data @NoArgsConstructor @AllArgsConstructor
public class SucursalDTO {
    private Long id;

    @NotBlank(message = "El nombre de la sucursal no puede ser nulo ni vacio")
    private String nombre;

    @NotBlank(message = "La direccion no puede ser nula ni vacia")
    private String direccion;

    @NotBlank(message = "La ciudad no puede ser nula ni vacia")
    private String ciudad;

    @NotBlank(message = "El telefono no puede ser nulo ni vacio")
    private String telefono;

    @NotBlank(message = "El horario no puede ser nulo ni vacio")
    private String horario;

    public static SucursalDTO fromEntity(Sucursal s) {
        return new SucursalDTO(s.getId(), s.getNombre(), s.getDireccion(),
            s.getCiudad(), s.getTelefono(), s.getHorario());
    }
    public Sucursal toEntity() {
        return new Sucursal(id, nombre, direccion, ciudad, telefono, horario);
    }
}
