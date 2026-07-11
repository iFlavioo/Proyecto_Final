package com.bookpoint.usuario.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import com.bookpoint.usuario.model.Usuario;

@Data @NoArgsConstructor @AllArgsConstructor
public class UsuarioDTO {
    private Long id;

    @NotBlank(message = "El nombre no puede ser nulo ni vacio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El email no puede ser nulo ni vacio")
    @Email(message = "El email no tiene un formato valido")
    private String email;

    @NotBlank(message = "La password no puede ser nula ni vacia")
    @Size(min = 4, message = "La password debe tener al menos 4 caracteres")
    private String password;

    @NotBlank(message = "El rol no puede ser nulo ni vacio")
    @Pattern(regexp = "ADMIN|CLIENTE|VENDEDOR|LOGISTICA|BODEGA",
             message = "Rol invalido. Use: ADMIN, CLIENTE, VENDEDOR, LOGISTICA o BODEGA")
    private String rol;

    public static UsuarioDTO fromEntity(Usuario u) {
        return new UsuarioDTO(u.getId(), u.getNombre(), u.getEmail(), u.getPassword(), u.getRol());
    }
    public Usuario toEntity() {
        return new Usuario(id, nombre, email, password, rol);
    }
}
