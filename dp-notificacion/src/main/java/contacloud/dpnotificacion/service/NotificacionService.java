package contacloud.dpnotificacion.service;

import contacloud.dpnotificacion.entity.Notificacion;

import java.util.List;
import java.util.Optional;

public interface NotificacionService {

    List<Notificacion> listar();
    Optional<Notificacion> buscar(Integer id);
    Notificacion guardar(Notificacion notificacion);
    Notificacion actualizar(Integer id , String estado);
    String sendEmail(Integer notificacionId,Integer clienteId);


}
