package com.bookpoint.inventario.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bookpoint.inventario.model.Inventario;
import com.bookpoint.inventario.repository.InventarioRepository;

@Service
public class InventarioService {
    @Autowired private InventarioRepository inventarioRepository;
    @Autowired private RestTemplate restTemplate;

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

    public Inventario guardarInventario(Inventario inventario) {
        if (!existe(productoUrl + "/api/v1/productos/" + inventario.getProductoId())) {
            throw new RuntimeException("No existe producto con id: " + inventario.getProductoId());
        }
        if (!existe(sucursalUrl + "/api/v1/sucursales/" + inventario.getSucursalId())) {
            throw new RuntimeException("No existe sucursal con id: " + inventario.getSucursalId());
        }
        return inventarioRepository.save(inventario);
    }

    public List<Inventario> listarInventarios() { return inventarioRepository.findAll(); }

    public Optional<Inventario> obtenerInventarioPorId(Long id) { return inventarioRepository.findById(id); }

    public Inventario actualizarInventario(Long id, Inventario inventario) {
        Inventario existente = inventarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No existe inventario con id: " + id));
        if (!existe(productoUrl + "/api/v1/productos/" + inventario.getProductoId())) {
            throw new RuntimeException("No existe producto con id: " + inventario.getProductoId());
        }
        if (!existe(sucursalUrl + "/api/v1/sucursales/" + inventario.getSucursalId())) {
            throw new RuntimeException("No existe sucursal con id: " + inventario.getSucursalId());
        }
        existente.setProductoId(inventario.getProductoId());
        existente.setSucursalId(inventario.getSucursalId());
        existente.setStock(inventario.getStock());
        existente.setStockMinimo(inventario.getStockMinimo());
        return inventarioRepository.save(existente);
    }

    public void eliminarInventario(Long id) { inventarioRepository.deleteById(id); }

    /** Descuenta stock al vender. Llamado desde venta-service. */
    public void descontarStock(Long productoId, Long sucursalId, Integer cantidad) {
        Inventario inv = inventarioRepository.findByProductoIdAndSucursalId(productoId, sucursalId)
            .orElseThrow(() -> new RuntimeException(
                "No hay inventario para productoId=" + productoId + " sucursalId=" + sucursalId));
        if (inv.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + inv.getStock() + ", requerido: " + cantidad);
        }
        inv.setStock(inv.getStock() - cantidad);
        inventarioRepository.save(inv);
    }

    /** Consulta el inventario de un producto en una sucursal especifica. Usado por venta-service. */
    public Optional<Inventario> consultarStock(Long productoId, Long sucursalId) {
        return inventarioRepository.findByProductoIdAndSucursalId(productoId, sucursalId);
    }
}
