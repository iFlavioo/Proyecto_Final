package com.bookpoint.producto.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import com.bookpoint.producto.model.Producto;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProductoDTO {
    private Long id;

    @NotBlank(message = "El nombre del producto no puede ser nulo ni vacio")
    @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
    private String nombre;

    @NotBlank(message = "La descripcion no puede ser nula ni vacia")
    @Size(max = 500, message = "La descripcion no puede superar 500 caracteres")
    private String descripcion;

    @NotNull(message = "El precio no puede ser nulo ni vacio")
    @Positive(message = "El precio debe ser mayor a cero")
    private Double precio;

    @NotBlank(message = "La categoria no puede ser nula ni vacia")
    private String categoria;

    @NotBlank(message = "El autor no puede ser nulo ni vacio")
    private String autor;

    @NotBlank(message = "La editorial no puede ser nula ni vacia")
    private String editorial;

    public static ProductoDTO fromEntity(Producto p) {
        return new ProductoDTO(p.getId(), p.getNombre(), p.getDescripcion(),
            p.getPrecio(), p.getCategoria(), p.getAutor(), p.getEditorial());
    }
    public Producto toEntity() {
        return new Producto(id, nombre, descripcion, precio, categoria, autor, editorial);
    }
}
