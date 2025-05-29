package contacloud.dplicencias.repository;

import contacloud.dplicencias.entity.Licencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenciaRepository extends JpaRepository<Licencia, Integer> {
}
