package com.bookpoint.despacho.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bookpoint.despacho.model.Despacho;
import com.bookpoint.despacho.repository.DespachoRepository;

@Service
public class DespachoService {
    @Autowired private DespachoRepository despachoRepository;
    @Autowired private RestTemplate restTemplate;

    @Value("${services.pedido-service.url:http://localhost:8087}")
    private String pedidoUrl;
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

    public Despacho guardarDespacho(Despacho despacho) {
        if (!existe(pedidoUrl + "/api/v1/pedidos/" + despacho.getPedidoId())) {
            throw new RuntimeException("No existe pedido con id: " + despacho.getPedidoId());
        }
        if (!existe(sucursalUrl + "/api/v1/sucursales/" + despacho.getSucursalId())) {
            throw new RuntimeException("No existe sucursal con id: " + despacho.getSucursalId());
        }
        return despachoRepository.save(despacho);
    }

    public List<Despacho> listarDespachos() { return despachoRepository.findAll(); }

    public Optional<Despacho> obtenerDespachoPorId(Long id) { return despachoRepository.findById(id); }

    public Despacho actualizarDespacho(Long id, Despacho despacho) {
        Despacho existente = despachoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No existe despacho con id: " + id));
        if (!existe(pedidoUrl + "/api/v1/pedidos/" + despacho.getPedidoId())) {
            throw new RuntimeException("No existe pedido con id: " + despacho.getPedidoId());
        }
        if (!existe(sucursalUrl + "/api/v1/sucursales/" + despacho.getSucursalId())) {
            throw new RuntimeException("No existe sucursal con id: " + despacho.getSucursalId());
        }
        existente.setPedidoId(despacho.getPedidoId());
        existente.setSucursalId(despacho.getSucursalId());
        existente.setEstado(despacho.getEstado());
        existente.setFechaDespacho(despacho.getFechaDespacho());
        existente.setDireccionDestino(despacho.getDireccionDestino());
        return despachoRepository.save(existente);
    }

    public void eliminarDespacho(Long id) { despachoRepository.deleteById(id); }
}
