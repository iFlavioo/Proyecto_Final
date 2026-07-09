package com.bookpoint.proveedor.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import com.bookpoint.proveedor.model.Proveedor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProveedorDTO {
    private Long id;

    @NotBlank(message = "El nombre del proveedor es obligatorio")
    private String nombre;

    @Email(message = "El email no tiene un formato valido")
    private String email;

    private String telefono;
    private String editorial;
    private Boolean activo;

    public static ProveedorDTO fromEntity(Proveedor p) {
        return new ProveedorDTO(p.getId(), p.getNombre(), p.getEmail(),
            p.getTelefono(), p.getEditorial(), p.getActivo());
    }
    public Proveedor toEntity() {
        return new Proveedor(id, nombre, email, telefono, editorial, activo);
    }
}
