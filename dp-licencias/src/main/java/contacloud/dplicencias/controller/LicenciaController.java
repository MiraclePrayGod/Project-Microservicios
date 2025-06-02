package contacloud.dplicencias.controller;


import contacloud.dplicencias.dto.LicenciaCreateDto;
import contacloud.dplicencias.entity.Licencia;
import contacloud.dplicencias.service.LicenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/licencias")
public class LicenciaController {

    @Autowired
    private LicenciaService licenciaService;

    @GetMapping
    public ResponseEntity<List<Licencia>> listarLicencias() {
        List<Licencia> licencias = licenciaService.listar();
        return ResponseEntity.ok(licencias);  // Retorna una lista de licencias
    }

    // Endpoint para buscar una licencia por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Licencia> obtenerLicencia(@PathVariable Integer id) {
        Optional<Licencia> licencia = licenciaService.buscar(id);
        return licencia.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para crear una nueva licencia
    @PostMapping
    public ResponseEntity<Licencia> crearLicencia(@RequestBody LicenciaCreateDto licenciaDto) {
        Licencia nuevaLicencia = licenciaService.guardar(licenciaDto);  // Crear la nueva licencia
        return ResponseEntity.status(201).body(nuevaLicencia);  // Retorna la licencia creada con estado 201
    }

    // Endpoint para actualizar una licencia existente
    @PutMapping("/{id}")
    public ResponseEntity<Licencia> actualizarLicencia(@PathVariable Integer id, @RequestBody Licencia licencia) {
        // Actualizar licencia
        Licencia licenciaActualizada = licenciaService.actualizar(id, licencia);
        return ResponseEntity.ok(licenciaActualizada);  // Retorna la licencia actualizada
    }

    // Endpoint para eliminar una licencia por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLicencia(@PathVariable Integer id) {
        licenciaService.eliminar(id);  // Eliminar la licencia
        return ResponseEntity.noContent().build();  // Retorna un código de estado 204 (sin contenido)
    }

}


