package contacloud.dppagos.controller;


import contacloud.dppagos.entity.Pagos;
import contacloud.dppagos.service.PagosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/pagos")
@RestController

public class PagosController {
    @Autowired
    private PagosService pagosService;

    @GetMapping
    public ResponseEntity<List<Pagos>> listar(){
        List<Pagos> pagos = pagosService.listar();
        return ResponseEntity.ok(pagos);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Pagos> buscar(@PathVariable Integer id){
        return pagosService.buscar(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pagos> guardar(@RequestBody Pagos pagos){
        Pagos pagoGuardado=pagosService.guardar(pagos);
        return ResponseEntity.status(201).body(pagoGuardado);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Pagos> actualizar(@PathVariable Integer id,@RequestBody Pagos pagos){
        Pagos pagoActualizado = pagosService.actualizar(id,pagos);
        return ResponseEntity.ok(pagoActualizado);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        pagosService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
