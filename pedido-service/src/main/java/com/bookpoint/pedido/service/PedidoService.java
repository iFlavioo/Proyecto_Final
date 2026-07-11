package com.bookpoint.pedido.service;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bookpoint.pedido.model.Pedido;
import com.bookpoint.pedido.repository.PedidoRepository;

@Service
public class PedidoService {
    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);
    private static final String ESTADOS_VALIDOS = "PENDIENTE|ENVIADO|ENTREGADO|CANCELADO";

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private RestTemplate restTemplate;

    private void validarPedido(Pedido pedido) {
        if (pedido.getEstado() == null || pedido.getEstado().trim().isEmpty()) {
            log.error("estado no válido: no puede estar vacío ni en blanco");
            throw new IllegalArgumentException("El estado no puede estar vacío ni en blanco");
        }
        if (!pedido.getEstado().matches(ESTADOS_VALIDOS)) {
            log.error("estado no válido: {}", pedido.getEstado());
            throw new IllegalArgumentException("Estado invalido. Use: PENDIENTE, ENVIADO, ENTREGADO o CANCELADO");
        }
        if (pedido.getDireccionEntrega() == null || pedido.getDireccionEntrega().trim().isEmpty()) {
            log.error("direccionEntrega no válido: no puede estar vacío ni en blanco");
            throw new IllegalArgumentException("La direccion de entrega no puede estar vacía ni en blanco");
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

    public Pedido guardarPedido(Pedido pedido) {
        validarPedido(pedido);
        if (!existe(usuarioUrl + "/api/v1/usuarios/" + pedido.getUsuarioId())) {
            throw new RuntimeException("No existe usuario con id: " + pedido.getUsuarioId());
        }
        if (!existe(productoUrl + "/api/v1/productos/" + pedido.getProductoId())) {
            throw new RuntimeException("No existe producto con id: " + pedido.getProductoId());
        }
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPedidos() { return pedidoRepository.findAll(); }

    public Optional<Pedido> obtenerPedidoPorId(Long id) { return pedidoRepository.findById(id); }

    public Pedido actualizarPedido(Long id, Pedido pedido) {
        validarPedido(pedido);
        Pedido existente = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No existe pedido con id: " + id));
        if (!existe(usuarioUrl + "/api/v1/usuarios/" + pedido.getUsuarioId())) {
            throw new RuntimeException("No existe usuario con id: " + pedido.getUsuarioId());
        }
        if (!existe(productoUrl + "/api/v1/productos/" + pedido.getProductoId())) {
            throw new RuntimeException("No existe producto con id: " + pedido.getProductoId());
        }
        existente.setUsuarioId(pedido.getUsuarioId());
        existente.setProductoId(pedido.getProductoId());
        existente.setCantidad(pedido.getCantidad());
        existente.setEstado(pedido.getEstado());
        existente.setFechaPedido(pedido.getFechaPedido());
        existente.setDireccionEntrega(pedido.getDireccionEntrega());
        return pedidoRepository.save(existente);
    }

    public void eliminarPedido(Long id) { pedidoRepository.deleteById(id); }
}
