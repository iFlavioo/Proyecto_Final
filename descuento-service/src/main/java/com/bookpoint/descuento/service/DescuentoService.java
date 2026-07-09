package com.bookpoint.descuento.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bookpoint.descuento.model.Descuento;
import com.bookpoint.descuento.repository.DescuentoRepository;
@Service
public class DescuentoService {
    @Autowired private DescuentoRepository descuentoRepository;

    public Descuento guardarDescuento(Descuento descuento) { return descuentoRepository.save(descuento); }
    public List<Descuento> listarDescuentos() { return descuentoRepository.findAll(); }
    public Optional<Descuento> obtenerDescuentoPorId(Long id) { return descuentoRepository.findById(id); }
    public Descuento actualizarDescuento(Long id, Descuento descuento) {
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
