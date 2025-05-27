package contacloud.dppagos.service.impl;


import contacloud.dppagos.entity.Pagos;
import contacloud.dppagos.repository.PagoRepository;
import contacloud.dppagos.service.PagosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagosServiceImpl implements PagosService {


    @Autowired
    private PagoRepository pagoRepository;

    @Override
    public List<Pagos> listar() {
        return pagoRepository.findAll();
    }

    @Override
    public Optional<Pagos> buscar(Integer id) {
        if (!pagoRepository.existsById(id)) {
        throw new IllegalArgumentException("No existe un pago con el id " + id);

        }
        return pagoRepository.findById(id);
    }

    @Override
    public Pagos guardar(Pagos pago) {
        if (pagoRepository.existsByReferencia(pago.getReferencia())) {
            throw new DataIntegrityViolationException("Ya existe un pago con referencia:  " + pago.getReferencia());
        }
        return pagoRepository.save(pago);
    }

    @Override
    public Pagos actualizar(Integer id, Pagos pago) {
        if (!pagoRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe un pago con el id " + id);
        }
        pago.setPagoId(id);
        return pagoRepository.save(pago);
    }

    @Override
    public void eliminar(Integer id) {
        if (!pagoRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe un pago con el id " + id);
        }
        pagoRepository.deleteById(id);
    }
}
