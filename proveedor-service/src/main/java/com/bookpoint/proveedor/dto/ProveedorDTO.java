package com.bookpoint.proveedor.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import com.bookpoint.proveedor.model.Proveedor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProveedorDTO {
    private Long id;

    @NotBlank(message = "El nombre del proveedor no puede ser nulo ni vacio")
    private String nombre;

    @NotBlank(message = "El email no puede ser nulo ni vacio")
    @Email(message = "El email no tiene un formato valido")
    private String email;

    @NotBlank(message = "El telefono no puede ser nulo ni vacio")
    private String telefono;

    @NotBlank(message = "La editorial no puede ser nula ni vacia")
    private String editorial;

    @NotNull(message = "El campo activo no puede ser nulo")
    private Boolean activo;

    public static ProveedorDTO fromEntity(Proveedor p) {
        return new ProveedorDTO(p.getId(), p.getNombre(), p.getEmail(),
            p.getTelefono(), p.getEditorial(), p.getActivo());
    }
    public Proveedor toEntity() {
        return new Proveedor(id, nombre, email, telefono, editorial, activo);
    }
}
