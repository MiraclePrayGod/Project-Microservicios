package com.contacloud.pdinventario.service;

import com.contacloud.pdinventario.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {


    public List<Producto> listar() {
    }

    public Optional<Producto> obtenerPorId(Long id) {
    }

    public Producto guardar(Producto producto) {
    }

    public Producto actualizarStock(Long id, int cantidad) {

    }

     void eliminar(Long id) {
    }
}
