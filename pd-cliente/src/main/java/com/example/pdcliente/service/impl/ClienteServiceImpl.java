package com.example.pdcliente.service.impl;

import com.example.pdcliente.entity.Cliente;
import com.example.pdcliente.repository.ClienteRepository;
import com.example.pdcliente.service.ClienteService;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.events.MappingEndEvent;

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
    String NombreCapitalizado = capitalizarNombre(cliente.getNombre());
        cliente.setNombre(NombreCapitalizado);
        cliente.setDireccion(cliente.getDireccion());
        cliente.setEmail(cliente.getEmail());
        cliente.setTelefono(cliente.getTelefono());
        cliente.setFecha(LocalDateTime.now());
        cliente.setEstado(cliente.getEstado());

        return clienteRepository.save(cliente);
    }


    @Override
    public Cliente actualizar(Integer id, Cliente cliente) {

//        if (!clienteRepository.existsById(id)){
//            throw new IllegalArgumentException("Cliente con referencia "+id+" no existe");
//        }
        Cliente clienteExistente = clienteRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Cliente con referencia id : "+
                id + " no existe "));

        clienteExistente.setDireccion( cliente.getDireccion());
        clienteExistente.setEmail(cliente.getEmail());
        clienteExistente.setTelefono(cliente.getTelefono());
        clienteExistente.setEstado(cliente.getEstado());
        clienteExistente.setNombre(cliente.getNombre());
        clienteExistente.setRucDni(cliente.getRucDni());

        return clienteRepository.save(clienteExistente);
    }

    @Override
      public void eliminar(Integer id) {
        if (!clienteRepository.existsById(id)){
            throw new IllegalArgumentException("Cliente con id "+id+" no existe");
        }
         clienteRepository.deleteById(id);
    }

    public String capitalizarNombre(String nombre){
        if (nombre==null||nombre.isEmpty()){
            return  nombre;
        }
        String[] palabras = nombre.split(" ");
        StringBuilder nombreCapitalizado = new StringBuilder();
        for (String palabra: palabras){
            if(palabra.length() > 0 ){
                nombreCapitalizado.append(palabra.substring(0,1).toUpperCase())
                        .append(palabra.substring(1).toLowerCase())
                        .append(" ");
            }
        }
            return nombreCapitalizado.toString().trim();
    }
}
