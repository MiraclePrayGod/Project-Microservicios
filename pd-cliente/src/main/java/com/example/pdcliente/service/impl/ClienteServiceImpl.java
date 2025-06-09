package com.example.pdcliente.service.impl;

import com.example.pdcliente.entity.Cliente;
import com.example.pdcliente.repository.ClienteRepository;
import com.example.pdcliente.service.ClienteService;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private MeterFilter metricsHttpServerUriTagFilter;

    @Override
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> buscar(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("El cliente con id " + id +" no existe");
        }

       return clienteRepository.findById(id);
    }

    @Override
    public Cliente guardar(Cliente cliente) {
    if (clienteRepository.existsByRucDni(cliente.getRucDni())){
            throw new DataIntegrityViolationException("Ya existe un cliente con ese RUC/DNI");
        }

        cliente.setFecha(LocalDateTime.now());


        return clienteRepository.save(cliente);
    }


    @Override
    public Cliente actualizar(Integer id, Cliente cliente) {
        if (!clienteRepository.existsById(id)){
            throw new IllegalArgumentException("Cliente con referencia "+id+" no existe");
        }
        cliente.setId(id);
        return clienteRepository.save(cliente);
    }

    @Override
      public void eliminar(Integer id) {
        if (!clienteRepository.existsById(id)){
            throw new IllegalArgumentException("Cliente con id "+id+" no existe");
        }
         clienteRepository.deleteById(id);
    }
}
