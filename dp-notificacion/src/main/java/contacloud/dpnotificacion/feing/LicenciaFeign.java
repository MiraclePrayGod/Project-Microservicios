package contacloud.dpnotificacion.feing;

import contacloud.dpnotificacion.dto.ClienteDto;
import contacloud.dpnotificacion.dto.LicenciaDato;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "dp-licencias", path = "/licencias")
public interface LicenciaFeign {
    @GetMapping("/{id}")
    ResponseEntity<LicenciaDato> obtenerPorId(@PathVariable Integer id);

    @PutMapping("/{id}")
    ResponseEntity<LicenciaDato> actualizarCurso(@PathVariable Integer id, @RequestBody ClienteDto cursoDto);

}
