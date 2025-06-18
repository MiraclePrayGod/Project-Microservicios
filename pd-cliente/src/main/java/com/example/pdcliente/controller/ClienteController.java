        package com.example.pdcliente.controller;

        import com.example.pdcliente.entity.Cliente;
        import com.example.pdcliente.service.ClienteService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;

        import java.util.List;
        @RestController
        @RequestMapping("/clientes")
        public class ClienteController  {

            @Autowired
            private ClienteService clienteService;

            @GetMapping
            public ResponseEntity<List<Cliente>> listar(){
                List<Cliente> clientes = clienteService.listar();
                return ResponseEntity.ok(clientes);
            }
            @GetMapping("/{id}")
            public ResponseEntity<Cliente> buscar(@PathVariable Integer id){
                return clienteService.buscar(id)
                        .map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
            }

            @PostMapping
            public ResponseEntity<Cliente> guardar(@RequestBody Cliente cliente){
                Cliente clienteGuardado=clienteService.guardar(cliente);
                return ResponseEntity.status(201).body(clienteGuardado);
            }
            @PutMapping("/{id}")
            public ResponseEntity<Cliente> actualizar(@PathVariable Integer id,@RequestBody Cliente cliente){
                Cliente clienteActualizado = clienteService.actualizar(id,cliente);
                return ResponseEntity.ok(clienteActualizado);
            }
            @DeleteMapping("/{id}")
            public ResponseEntity<Void> eliminar(@PathVariable Integer id){
                clienteService.eliminar(id);
                return ResponseEntity.noContent().build();
            }

            @PutMapping("/habilitar/{clienteId}")
            public ResponseEntity<Void> habilitarCliente(@PathVariable Integer clienteId, String estado) {
                // Aquí actualizas el estado del cliente a "habilitado"
                clienteService.habilitarCliente(clienteId,estado);
                return ResponseEntity.ok().build();
            }

        }
