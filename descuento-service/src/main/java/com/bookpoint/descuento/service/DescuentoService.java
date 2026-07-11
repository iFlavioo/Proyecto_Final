package com.bookpoint.descuento.service;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bookpoint.descuento.model.Descuento;
import com.bookpoint.descuento.repository.DescuentoRepository;
@Service
public class DescuentoService {
    private static final Logger log = LoggerFactory.getLogger(DescuentoService.class);
    private static final String TIPOS_VALIDOS = "CUPON|CONVENIO_ESTUDIANTIL|PROMOCION";

    @Autowired private DescuentoRepository descuentoRepository;

    private void validarDescuento(Descuento descuento) {
        if (descuento.getCodigo() == null || descuento.getCodigo().trim().isEmpty()) {
            log.error("codigo no válido: no puede estar vacío ni en blanco");
            throw new IllegalArgumentException("El codigo no puede estar vacío ni en blanco");
        }
        if (descuento.getTipo() != null && !descuento.getTipo().trim().isEmpty()
                && !descuento.getTipo().matches(TIPOS_VALIDOS)) {
            log.error("tipo no válido: {}", descuento.getTipo());
            throw new IllegalArgumentException("Tipo invalido. Use: CUPON, CONVENIO_ESTUDIANTIL o PROMOCION");
        }
    }

    public Descuento guardarDescuento(Descuento descuento) {
        validarDescuento(descuento);
        return descuentoRepository.save(descuento);
    }
    public List<Descuento> listarDescuentos() { return descuentoRepository.findAll(); }
    public Optional<Descuento> obtenerDescuentoPorId(Long id) { return descuentoRepository.findById(id); }
    public Descuento actualizarDescuento(Long id, Descuento descuento) {
        validarDescuento(descuento);
        Descuento existente = descuentoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No existe descuento con id: " + id));
        existente.setCodigo(descuento.getCodigo());
        existente.setDescripcion(descuento.getDescripcion());
        existente.setPorcentaje(descuento.getPorcentaje());
        existente.setTipo(descuento.getTipo());
        existente.setActivo(descuento.getActivo());
        existente.setFechaExpiracion(descuento.getFechaExpiracion());
        return descuentoRepository.save(existente);
    }
    public void eliminarDescuento(Long id) { descuentoRepository.deleteById(id); }
}
