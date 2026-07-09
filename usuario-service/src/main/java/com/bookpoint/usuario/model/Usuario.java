package com.bookpoint.usuario.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "usuarios")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato valido")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "La password es obligatoria")
    @Size(min = 4, message = "La password debe tener al menos 4 caracteres")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "ADMIN|CLIENTE|VENDEDOR|LOGISTICA|BODEGA",
             message = "Rol invalido. Use: ADMIN, CLIENTE, VENDEDOR, LOGISTICA o BODEGA")
    @Column(name = "rol", nullable = false)
    private String rol;
}
