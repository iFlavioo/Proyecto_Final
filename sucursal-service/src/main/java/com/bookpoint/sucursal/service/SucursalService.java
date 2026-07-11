package com.bookpoint.sucursal.service;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bookpoint.sucursal.model.Sucursal;
import com.bookpoint.sucursal.repository.SucursalRepository;
@Service
public class SucursalService {
    private static final Logger log = LoggerFactory.getLogger(SucursalService.class);
    private static final String CIUDADES_VALIDAS = "Concepcion|Temuco|La Serena";

    @Autowired private SucursalRepository sucursalRepository;

    private void validarSucursal(Sucursal sucursal) {
        if (sucursal.getNombre() == null || sucursal.getNombre().trim().isEmpty()) {
            log.error("nombre no válido: no puede estar vacío ni en blanco");
            throw new IllegalArgumentException("El nombre no puede estar vacío ni en blanco");
        }
        if (sucursal.getDireccion() == null || sucursal.getDireccion().trim().isEmpty()) {
            log.error("direccion no válido: no puede estar vacío ni en blanco");
            throw new IllegalArgumentException("La direccion no puede estar vacía ni en blanco");
        }
        if (sucursal.getCiudad() == null || sucursal.getCiudad().trim().isEmpty()) {
            log.error("ciudad no válido: no puede estar vacío ni en blanco");
            throw new IllegalArgumentException("La ciudad no puede estar vacía ni en blanco");
        }
        if (!sucursal.getCiudad().matches(CIUDADES_VALIDAS)) {
            log.error("ciudad no válido: {}", sucursal.getCiudad());
            throw new IllegalArgumentException("Ciudad invalida. Use: Concepcion, Temuco o La Serena");
        }
        if (sucursal.getTelefono() != null && !sucursal.getTelefono().trim().isEmpty()
                && !sucursal.getTelefono().matches("\\d{9}")) {
            log.error("telefono no válido: {}", sucursal.getTelefono());
            throw new IllegalArgumentException("El telefono debe tener 9 digitos");
        }
    }

    public Sucursal guardarSucursal(Sucursal sucursal) {
        validarSucursal(sucursal);
        return sucursalRepository.save(sucursal);
    }
    public List<Sucursal> listarSucursals() { return sucursalRepository.findAll(); }
    public Optional<Sucursal> obtenerSucursalPorId(Long id) { return sucursalRepository.findById(id); }
    public Sucursal actualizarSucursal(Long id, Sucursal sucursal) {
        validarSucursal(sucursal);
        Sucursal existente = sucursalRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No existe sucursal con id: " + id));
        existente.setNombre(sucursal.getNombre());
        existente.setDireccion(sucursal.getDireccion());
        existente.setCiudad(sucursal.getCiudad());
        existente.setTelefono(sucursal.getTelefono());
        existente.setHorario(sucursal.getHorario());
        return sucursalRepository.save(existente);
    }
    public void eliminarSucursal(Long id) { sucursalRepository.deleteById(id); }
}
