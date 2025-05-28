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

    @PostMapping("/enviar/{notificacionId}/{clienteId}")
    public ResponseEntity<String> sendEmail(@PathVariable Integer notificacionId, @PathVariable Integer clienteId) {
        try {
            // Validación básica de los parámetros (ajustar según lo que necesites)
            if (notificacionId == null || clienteId == null) {
                return ResponseEntity.status(400).body("Los parámetros no son válidos.");
            }

            // Llamada al servicio que envía el correo
            String token = notificacionService.sendEmail(notificacionId, clienteId);

            // Respuesta exitosa con código 200
            return ResponseEntity.status(200).body("Correo enviado con éxito. Token generado: " + token);

        } catch (Exception e) {
            // Respuesta con código 500 en caso de error
            return ResponseEntity.status(500).body("Error al enviar el correo: " + e.getMessage());
        }
    }


}
