package com.bookpoint.notificacion.service;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bookpoint.notificacion.model.Notificacion;
import com.bookpoint.notificacion.repository.NotificacionRepository;

@Service
public class NotificacionService {
    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    @Autowired private NotificacionRepository notificacionRepository;
    @Autowired private RestTemplate restTemplate;

    private void validarNotificacion(Notificacion notificacion) {
        if (notificacion.getMensaje() == null || notificacion.getMensaje().trim().isEmpty()) {
            log.error("mensaje no válido: no puede estar vacío ni en blanco");
            throw new IllegalArgumentException("El mensaje no puede estar vacío ni en blanco");
        }
    }

    @Value("${services.usuario-service.url:http://localhost:8081}")
    private String usuarioUrl;

    private boolean existe(String url) {
        try {
            ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
            return resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    public Notificacion guardarNotificacion(Notificacion notificacion) {
        validarNotificacion(notificacion);
        if (!existe(usuarioUrl + "/api/v1/usuarios/" + notificacion.getUsuarioId())) {
            throw new RuntimeException("No existe usuario con id: " + notificacion.getUsuarioId());
        }
        return notificacionRepository.save(notificacion);
    }

    public List<Notificacion> listarNotificacions() { return notificacionRepository.findAll(); }

    public Optional<Notificacion> obtenerNotificacionPorId(Long id) { return notificacionRepository.findById(id); }

    public Notificacion actualizarNotificacion(Long id, Notificacion notificacion) {
        validarNotificacion(notificacion);
        Notificacion existente = notificacionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No existe notificacion con id: " + id));
        if (!existe(usuarioUrl + "/api/v1/usuarios/" + notificacion.getUsuarioId())) {
            throw new RuntimeException("No existe usuario con id: " + notificacion.getUsuarioId());
        }
        existente.setUsuarioId(notificacion.getUsuarioId());
        existente.setMensaje(notificacion.getMensaje());
        existente.setTipo(notificacion.getTipo());
        existente.setLeida(notificacion.getLeida());
        existente.setFechaCreacion(notificacion.getFechaCreacion());
        return notificacionRepository.save(existente);
    }

    public void eliminarNotificacion(Long id) { notificacionRepository.deleteById(id); }
}
