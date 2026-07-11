package com.bookpoint.resena.service;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bookpoint.resena.model.Resena;
import com.bookpoint.resena.repository.ResenaRepository;

@Service
public class ResenaService {
    private static final Logger log = LoggerFactory.getLogger(ResenaService.class);

    @Autowired private ResenaRepository resenaRepository;
    @Autowired private RestTemplate restTemplate;

    private void validarResena(Resena resena) {
        if (resena.getComentario() == null || resena.getComentario().trim().isEmpty()) {
            log.error("comentario no válido: no puede estar vacío ni en blanco");
            throw new IllegalArgumentException("El comentario no puede estar vacío ni en blanco");
        }
    }

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

    public Resena guardarResena(Resena resena) {
        validarResena(resena);
        if (!existe(usuarioUrl + "/api/v1/usuarios/" + resena.getUsuarioId())) {
            throw new RuntimeException("No existe usuario con id: " + resena.getUsuarioId());
        }
        if (!existe(productoUrl + "/api/v1/productos/" + resena.getProductoId())) {
            throw new RuntimeException("No existe producto con id: " + resena.getProductoId());
        }
        return resenaRepository.save(resena);
    }

    public List<Resena> listarResenas() { return resenaRepository.findAll(); }

    public Optional<Resena> obtenerResenaPorId(Long id) { return resenaRepository.findById(id); }

    public Resena actualizarResena(Long id, Resena resena) {
        validarResena(resena);
        Resena existente = resenaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No existe resena con id: " + id));
        if (!existe(usuarioUrl + "/api/v1/usuarios/" + resena.getUsuarioId())) {
            throw new RuntimeException("No existe usuario con id: " + resena.getUsuarioId());
        }
        if (!existe(productoUrl + "/api/v1/productos/" + resena.getProductoId())) {
            throw new RuntimeException("No existe producto con id: " + resena.getProductoId());
        }
        existente.setUsuarioId(resena.getUsuarioId());
        existente.setProductoId(resena.getProductoId());
        existente.setComentario(resena.getComentario());
        existente.setCalificacion(resena.getCalificacion());
        existente.setFechaResena(resena.getFechaResena());
        return resenaRepository.save(existente);
    }

    public void eliminarResena(Long id) { resenaRepository.deleteById(id); }
}
