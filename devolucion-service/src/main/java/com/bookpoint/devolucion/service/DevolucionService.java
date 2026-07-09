package com.bookpoint.devolucion.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bookpoint.devolucion.model.Devolucion;
import com.bookpoint.devolucion.repository.DevolucionRepository;

@Service
public class DevolucionService {
    @Autowired private DevolucionRepository devolucionRepository;
    @Autowired private RestTemplate restTemplate;

    @Value("${services.usuario-service.url:http://localhost:8081}")
    private String usuarioUrl;
    @Value("${services.producto-service.url:http://localhost:8084}")
    private String productoUrl;

    private boolean existe(String url) {
        try {
            ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
            return resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    public Devolucion guardarDevolucion(Devolucion devolucion) {
        if (!existe(usuarioUrl + "/api/v1/usuarios/" + devolucion.getUsuarioId())) {
            throw new RuntimeException("No existe usuario con id: " + devolucion.getUsuarioId());
        }
        if (!existe(productoUrl + "/api/v1/productos/" + devolucion.getProductoId())) {
            throw new RuntimeException("No existe producto con id: " + devolucion.getProductoId());
        }
        return devolucionRepository.save(devolucion);
    }

    public List<Devolucion> listarDevolucions() { return devolucionRepository.findAll(); }

    public Optional<Devolucion> obtenerDevolucionPorId(Long id) { return devolucionRepository.findById(id); }

    public Devolucion actualizarDevolucion(Long id, Devolucion devolucion) {
        Devolucion existente = devolucionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No existe devolucion con id: " + id));
        if (!existe(usuarioUrl + "/api/v1/usuarios/" + devolucion.getUsuarioId())) {
            throw new RuntimeException("No existe usuario con id: " + devolucion.getUsuarioId());
        }
        if (!existe(productoUrl + "/api/v1/productos/" + devolucion.getProductoId())) {
            throw new RuntimeException("No existe producto con id: " + devolucion.getProductoId());
        }
        existente.setUsuarioId(devolucion.getUsuarioId());
        existente.setProductoId(devolucion.getProductoId());
        existente.setMotivo(devolucion.getMotivo());
        existente.setEstado(devolucion.getEstado());
        existente.setFechaDevolucion(devolucion.getFechaDevolucion());
        return devolucionRepository.save(existente);
    }

    public void eliminarDevolucion(Long id) { devolucionRepository.deleteById(id); }
}
