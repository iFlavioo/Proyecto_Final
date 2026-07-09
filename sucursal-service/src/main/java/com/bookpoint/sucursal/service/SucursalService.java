package com.bookpoint.sucursal.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bookpoint.sucursal.model.Sucursal;
import com.bookpoint.sucursal.repository.SucursalRepository;
@Service
public class SucursalService {
    @Autowired private SucursalRepository sucursalRepository;

    public Sucursal guardarSucursal(Sucursal sucursal) { return sucursalRepository.save(sucursal); }
    public List<Sucursal> listarSucursals() { return sucursalRepository.findAll(); }
    public Optional<Sucursal> obtenerSucursalPorId(Long id) { return sucursalRepository.findById(id); }
    public Sucursal actualizarSucursal(Long id, Sucursal sucursal) {
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
