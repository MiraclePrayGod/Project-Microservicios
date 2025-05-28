package com.contacloud.pdinventario.service;

import com.contacloud.pdinventario.model.MovimientoStock;
import com.contacloud.pdinventario.repository.MovimientoStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimientoStockService {

    @Autowired
    private MovimientoStockRepository movimientoRepo;

    public MovimientoStock registrarMovimiento(MovimientoStock movimiento) {
        return movimientoRepo.save(movimiento);
    }

    public List<MovimientoStock> listarPorProducto(Long productoId) {
        return movimientoRepo.findByProductoId(productoId);
    }
}
