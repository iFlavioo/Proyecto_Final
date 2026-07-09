package com.bookpoint.producto.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bookpoint.producto.model.Producto;
import com.bookpoint.producto.repository.ProductoRepository;
@Service
public class ProductoService {
    @Autowired private ProductoRepository productoRepository;

    public Producto guardarProducto(Producto producto) { return productoRepository.save(producto); }
    public List<Producto> listarProductos() { return productoRepository.findAll(); }
    public Optional<Producto> obtenerProductoPorId(Long id) { return productoRepository.findById(id); }
    public Producto actualizarProducto(Long id, Producto producto) {
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
