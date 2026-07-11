package com.bookpoint.transferencia.service;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bookpoint.transferencia.model.Transferencia;
import com.bookpoint.transferencia.repository.TransferenciaRepository;

@Service
public class TransferenciaService {
    private static final Logger log = LoggerFactory.getLogger(TransferenciaService.class);
    private static final String ESTADOS_VALIDOS = "PENDIENTE|APROBADA|RECHAZADA";

    @Autowired private TransferenciaRepository transferenciaRepository;
    @Autowired private RestTemplate restTemplate;

    private void validarTransferencia(Transferencia transferencia) {
        if (transferencia.getEstado() != null && !transferencia.getEstado().trim().isEmpty()
                && !transferencia.getEstado().matches(ESTADOS_VALIDOS)) {
            log.error("estado no válido: {}", transferencia.getEstado());
            throw new IllegalArgumentException("Estado invalido. Use: PENDIENTE, APROBADA o RECHAZADA");
        }
    }

    @Value("${services.producto-service.url:http://localhost:8084}")
    private String productoUrl;
    @Value("${services.sucursal-service.url:http://localhost:8089}")
    private String sucursalUrl;

    private boolean existe(String url) {
        try {
            ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
            return resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    public Transferencia guardarTransferencia(Transferencia transferencia) {
        validarTransferencia(transferencia);
        if (!existe(productoUrl + "/api/v1/productos/" + transferencia.getProductoId())) {
            throw new RuntimeException("No existe producto con id: " + transferencia.getProductoId());
        }
        if (!existe(sucursalUrl + "/api/v1/sucursales/" + transferencia.getSucursalOrigenId())) {
            throw new RuntimeException("No existe sucursal con id: " + transferencia.getSucursalOrigenId());
        }
        if (!existe(sucursalUrl + "/api/v1/sucursales/" + transferencia.getSucursalDestinoId())) {
            throw new RuntimeException("No existe sucursal con id: " + transferencia.getSucursalDestinoId());
        }
        return transferenciaRepository.save(transferencia);
    }

    public List<Transferencia> listarTransferencias() { return transferenciaRepository.findAll(); }

    public Optional<Transferencia> obtenerTransferenciaPorId(Long id) { return transferenciaRepository.findById(id); }

    public Transferencia actualizarTransferencia(Long id, Transferencia transferencia) {
        validarTransferencia(transferencia);
        Transferencia existente = transferenciaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No existe transferencia con id: " + id));
        if (!existe(productoUrl + "/api/v1/productos/" + transferencia.getProductoId())) {
            throw new RuntimeException("No existe producto con id: " + transferencia.getProductoId());
        }
        if (!existe(sucursalUrl + "/api/v1/sucursales/" + transferencia.getSucursalOrigenId())) {
            throw new RuntimeException("No existe sucursal con id: " + transferencia.getSucursalOrigenId());
        }
        if (!existe(sucursalUrl + "/api/v1/sucursales/" + transferencia.getSucursalDestinoId())) {
            throw new RuntimeException("No existe sucursal con id: " + transferencia.getSucursalDestinoId());
        }
        existente.setProductoId(transferencia.getProductoId());
        existente.setSucursalOrigenId(transferencia.getSucursalOrigenId());
        existente.setSucursalDestinoId(transferencia.getSucursalDestinoId());
        existente.setCantidad(transferencia.getCantidad());
        existente.setEstado(transferencia.getEstado());
        existente.setFechaTransferencia(transferencia.getFechaTransferencia());
        return transferenciaRepository.save(existente);
    }

    public void eliminarTransferencia(Long id) { transferenciaRepository.deleteById(id); }
}
