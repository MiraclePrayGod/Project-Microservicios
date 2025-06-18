package com.example.pdcliente.service.impl;

import com.example.pdcliente.entity.Cliente;
import com.example.pdcliente.repository.ClienteRepository;
import com.example.pdcliente.service.ClienteService;
import com.example.pdcliente.utils.StringUtils;
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
        cliente.setNombre(StringUtils.capitalizarNombre(cliente.getNombre()));
        cliente.setDireccion(cliente.getDireccion());
        cliente.setEmail(StringUtils.esCorreoValido(cliente.getEmail()));
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
        cliente.setEmail(StringUtils.esCorreoValido(cliente.getEmail()));
        clienteExistente.setEmail(StringUtils.esCorreoValido(cliente.getEmail()));
        clienteExistente.setTelefono(cliente.getTelefono());
        clienteExistente.setEstado(cliente.getEstado());
        clienteExistente.setNombre(StringUtils.capitalizarNombre(cliente.getNombre()));
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

    @Override
    public Cliente habilitarCliente(Integer id,String estado) {

        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()){
            Cliente cliente1 = cliente.get();
            cliente1.setEstado("Habilitado");
            clienteRepository.save(cliente1);
            return cliente1;

        }else{
            throw new IllegalArgumentException("El cliente con id " + id + " no ha sido encontrado");
        }
    }

}
