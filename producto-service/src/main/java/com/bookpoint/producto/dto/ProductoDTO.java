package com.bookpoint.producto.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import com.bookpoint.producto.model.Producto;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProductoDTO {
    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripcion no puede superar 500 caracteres")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    private Double precio;

    @NotBlank(message = "La categoria es obligatoria")
    private String categoria;

    private String autor;
    private String editorial;

    public static ProductoDTO fromEntity(Producto p) {
        return new ProductoDTO(p.getId(), p.getNombre(), p.getDescripcion(),
            p.getPrecio(), p.getCategoria(), p.getAutor(), p.getEditorial());
    }
    public Producto toEntity() {
        return new Producto(id, nombre, descripcion, precio, categoria, autor, editorial);
    }
}
