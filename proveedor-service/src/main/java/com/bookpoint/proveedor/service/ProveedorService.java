package com.bookpoint.proveedor.service;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bookpoint.proveedor.model.Proveedor;
import com.bookpoint.proveedor.repository.ProveedorRepository;
@Service
public class ProveedorService {
    private static final Logger log = LoggerFactory.getLogger(ProveedorService.class);

    @Autowired private ProveedorRepository proveedorRepository;

    private void validarProveedor(Proveedor proveedor) {
        if (proveedor.getNombre() == null || proveedor.getNombre().trim().isEmpty()) {
            log.error("nombre no válido: no puede estar vacío ni en blanco");
            throw new IllegalArgumentException("El nombre no puede estar vacío ni en blanco");
        }
        if (proveedor.getEmail() != null && !proveedor.getEmail().trim().isEmpty()
                && !proveedor.getEmail().contains("@")) {
            log.error("email no válido: {}", proveedor.getEmail());
            throw new IllegalArgumentException("El email debe contener un @");
        }
    }

    public Proveedor guardarProveedor(Proveedor proveedor) {
        validarProveedor(proveedor);
        return proveedorRepository.save(proveedor);
    }
    public List<Proveedor> listarProveedors() { return proveedorRepository.findAll(); }
    public Optional<Proveedor> obtenerProveedorPorId(Long id) { return proveedorRepository.findById(id); }
    public Proveedor actualizarProveedor(Long id, Proveedor proveedor) {
        validarProveedor(proveedor);
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
