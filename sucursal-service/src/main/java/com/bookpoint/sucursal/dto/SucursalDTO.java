package com.bookpoint.sucursal.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import com.bookpoint.sucursal.model.Sucursal;

@Data @NoArgsConstructor @AllArgsConstructor
public class SucursalDTO {
    private Long id;

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String nombre;

    @NotBlank(message = "La direccion es obligatoria")
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    private String telefono;
    private String horario;

    public static SucursalDTO fromEntity(Sucursal s) {
        return new SucursalDTO(s.getId(), s.getNombre(), s.getDireccion(),
            s.getCiudad(), s.getTelefono(), s.getHorario());
    }
    public Sucursal toEntity() {
        return new Sucursal(id, nombre, direccion, ciudad, telefono, horario);
    }
}
