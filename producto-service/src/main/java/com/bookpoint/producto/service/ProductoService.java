package com.bookpoint.producto.service;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bookpoint.producto.model.Producto;
import com.bookpoint.producto.repository.ProductoRepository;
@Service
public class ProductoService {
    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    @Autowired private ProductoRepository productoRepository;

    private void validarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            log.error("nombre no válido: no puede estar vacío ni en blanco");
            throw new IllegalArgumentException("El nombre no puede estar vacío ni en blanco");
        }
        if (producto.getCategoria() == null || producto.getCategoria().trim().isEmpty()) {
            log.error("categoria no válido: no puede estar vacío ni en blanco");
            throw new IllegalArgumentException("La categoria no puede estar vacía ni en blanco");
        }
    }

    public Producto guardarProducto(Producto producto) {
        validarProducto(producto);
        return productoRepository.save(producto);
    }
    public List<Producto> listarProductos() { return productoRepository.findAll(); }
    public Optional<Producto> obtenerProductoPorId(Long id) { return productoRepository.findById(id); }
    public Producto actualizarProducto(Long id, Producto producto) {
        validarProducto(producto);
        Producto existente = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No existe producto con id: " + id));
        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setPrecio(producto.getPrecio());
        existente.setCategoria(producto.getCategoria());
        existente.setAutor(producto.getAutor());
        existente.setEditorial(producto.getEditorial());
        return productoRepository.save(existente);
    }
    public void eliminarProducto(Long id) { productoRepository.deleteById(id); }
}
