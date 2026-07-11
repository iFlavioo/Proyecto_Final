package com.bookpoint.venta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.bookpoint.venta.dto.DescontarStockRequestDTO;
import com.bookpoint.venta.model.Venta;
import com.bookpoint.venta.model.VentaDetalle;
import com.bookpoint.venta.repository.VentaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VentaService {

    @Autowired private VentaRepository ventaRepository;
    @Autowired private RestTemplate restTemplate;
    @Autowired private ObjectMapper objectMapper;

    @Value("${services.usuario-service.url:http://localhost:8081}")
    private String usuarioUrl;

    @Value("${services.producto-service.url:http://localhost:8084}")
    private String productoUrl;

    @Value("${services.sucursal-service.url:http://localhost:8089}")
    private String sucursalUrl;

    @Value("${services.inventario-service.url:http://localhost:8085}")
    private String inventarioUrl;

    // ─── Helpers de comunicacion con otros microservicios ──────────────────

    /** Devuelve el body como JSON si la llamada responde 2xx, o null si no existe / hay error. */
    private JsonNode obtenerJson(String url) {
        try {
            ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                return null;
            }
            return objectMapper.readTree(resp.getBody());
        } catch (Exception e) {
            return null;
        }
    }

    private boolean existe(String url) {
        return obtenerJson(url) != null;
    }

    // ─── Logica principal ───────────────────────────────────────────────────

    @Transactional
    public Venta guardarVenta(Venta venta) {
        // 1) Validar que el usuario exista
        if (!existe(usuarioUrl + "/api/v1/usuarios/" + venta.getUsuarioId())) {
            throw new RuntimeException("No existe usuario con id: " + venta.getUsuarioId());
        }

        // 2) Validar que la sucursal exista
        if (!existe(sucursalUrl + "/api/v1/sucursales/" + venta.getSucursalId())) {
            throw new RuntimeException("No existe sucursal con id: " + venta.getSucursalId());
        }

        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            throw new RuntimeException("La venta debe tener al menos un producto");
        }

        // La fecha de la venta se asigna automaticamente con la fecha actual del servidor,
        // ignorando cualquier valor enviado por el cliente.
        venta.setFechaVenta(java.time.LocalDate.now());

        // 3) Para cada producto: validar que exista y obtener su precio REAL (no confiar en el cliente)
        double total = 0.0;
        for (VentaDetalle d : venta.getDetalles()) {
            JsonNode productoJson = obtenerJson(productoUrl + "/api/v1/productos/" + d.getProductoId());
            if (productoJson == null) {
                throw new RuntimeException("No existe producto con id: " + d.getProductoId());
            }
            if (!productoJson.has("precio") || productoJson.get("precio").isNull()) {
                throw new RuntimeException("El producto con id " + d.getProductoId() + " no tiene un precio valido");
            }
            double precioReal = productoJson.get("precio").asDouble();

            // 4) Validar que haya stock suficiente ANTES de descontar nada
            JsonNode invJson = obtenerJson(inventarioUrl + "/api/v1/inventario/consultar?productoId="
                + d.getProductoId() + "&sucursalId=" + venta.getSucursalId());
            if (invJson == null) {
                throw new RuntimeException("No hay inventario registrado para el producto id "
                    + d.getProductoId() + " en la sucursal id " + venta.getSucursalId());
            }
            int stockDisponible = invJson.get("stock").asInt();
            if (stockDisponible < d.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto id " + d.getProductoId()
                    + ". Disponible: " + stockDisponible + ", solicitado: " + d.getCantidad());
            }

            double sub = precioReal * d.getCantidad();
            d.setPrecioUnitario(precioReal);
            d.setSubtotal(sub);
            d.setVenta(venta);
            total += sub;
        }
        venta.setTotal(total);

        // 5) Guardar la venta ya validada
        Venta guardada = ventaRepository.save(venta);

        // 6) Descontar stock de cada producto vendido (ya se valido que hay suficiente)
        for (VentaDetalle d : guardada.getDetalles()) {
            DescontarStockRequestDTO req = new DescontarStockRequestDTO(
                d.getProductoId(), guardada.getSucursalId(), d.getCantidad());
            ResponseEntity<Void> resp = restTemplate.exchange(
                inventarioUrl + "/api/v1/inventario/descontar",
                org.springframework.http.HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(req),
                Void.class);
            if (!resp.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("No se pudo descontar el stock del producto id " + d.getProductoId());
            }
        }

        return guardada;
    }

    public List<Venta> listarVentas() { return ventaRepository.findAll(); }

    public Optional<Venta> obtenerVentaPorId(Long id) { return ventaRepository.findById(id); }

    public Venta actualizarVenta(Long id, Venta venta) {
        Venta existente = ventaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No existe venta con id: " + id));
        existente.setUsuarioId(venta.getUsuarioId());
        existente.setSucursalId(venta.getSucursalId());
        // La fecha de venta original se conserva; se asigna automaticamente al crear y no se modifica.
        return ventaRepository.save(existente);
    }

    public void eliminarVenta(Long id) { ventaRepository.deleteById(id); }
}
