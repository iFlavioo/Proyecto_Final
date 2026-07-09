package com.bookpoint.proveedor.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bookpoint.proveedor.model.Proveedor;
import com.bookpoint.proveedor.repository.ProveedorRepository;
@Service
public class ProveedorService {
    @Autowired private ProveedorRepository proveedorRepository;

    public Proveedor guardarProveedor(Proveedor proveedor) { return proveedorRepository.save(proveedor); }
    public List<Proveedor> listarProveedors() { return proveedorRepository.findAll(); }
    public Optional<Proveedor> obtenerProveedorPorId(Long id) { return proveedorRepository.findById(id); }
    public Proveedor actualizarProveedor(Long id, Proveedor proveedor) {
        Proveedor existente = proveedorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No existe proveedor con id: " + id));
        existente.setNombre(proveedor.getNombre());
        existente.setEmail(proveedor.getEmail());
        existente.setTelefono(proveedor.getTelefono());
        existente.setEditorial(proveedor.getEditorial());
        existente.setActivo(proveedor.getActivo());
        return proveedorRepository.save(existente);
    }
    public void eliminarProveedor(Long id) { proveedorRepository.deleteById(id); }
}
