package contacloud.dpnotificacion.repository;

import contacloud.dpnotificacion.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
}
