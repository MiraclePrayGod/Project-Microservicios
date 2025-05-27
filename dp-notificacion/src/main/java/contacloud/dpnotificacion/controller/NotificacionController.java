package contacloud.dpnotificacion.controller;

import contacloud.dpnotificacion.entity.Notificacion;
import contacloud.dpnotificacion.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {
    @Autowired
    private NotificacionService notificacionService;

    @GetMapping
    public ResponseEntity<List<Notificacion>> listar(){
        List<Notificacion> notificaciones = notificacionService.listar();
        return ResponseEntity.ok(notificaciones);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> buscar(@PathVariable Integer id){
        return notificacionService.buscar(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Notificacion> guardar(@RequestBody Notificacion notificacion){
        Notificacion NotificacionGuardada =notificacionService.guardar(notificacion);
        return ResponseEntity.status(201).body(NotificacionGuardada);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Notificacion> actualizar(@PathVariable Integer id,String estado){
        Notificacion NotificacionActualizado = notificacionService.actualizar(id,estado);
        return ResponseEntity.ok(NotificacionActualizado);
    }

}
