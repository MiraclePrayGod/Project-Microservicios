package contacloud.dpnotificacion.service.impl;

import contacloud.dpnotificacion.dto.ClienteDto;
import contacloud.dpnotificacion.entity.Notificacion;
import contacloud.dpnotificacion.feing.ClienteFeing;
import contacloud.dpnotificacion.repository.NotificacionRepository;
import contacloud.dpnotificacion.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

 import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class NotificacionServiceImpl implements NotificacionService {


    @Autowired
    private NotificacionRepository notificacionRepository;
    @Autowired
    private ClienteFeing clienteFeing;
    @Autowired
    private  JavaMailSender mailSender;


    public Notificacion enviarNotificacion(Integer clienteId, String asunto, String mensaje) {
        // Obtener datos del cliente
        ClienteDto cliente = clienteFeing.obtenerPorId(clienteId).getBody();

        if (cliente == null || cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
            throw new RuntimeException("Cliente no encontrado o sin email válido");
        }

        // Crear la notificación
        Notificacion notificacion = new Notificacion();
        notificacion.setEmail(cliente.getEmail());
        notificacion.setAsunto(asunto);
        notificacion.setMensaje(mensaje);
        notificacion.setEstado("PENDIENTE");
        notificacion.setFechaEnvio(LocalDateTime.now());

        // Guardar la notificación en la base de datos
        notificacion = notificacionRepository.save(notificacion);

        try {
            // Enviar email
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(cliente.getEmail());
            mail.setSubject(asunto);
            mail.setText(mensaje);
            mailSender.send(mail);

            notificacion.setEstado("ENVIADO");
        } catch (Exception e) {
            notificacion.setEstado("FALLIDO");
            // Loguear la excepción si es necesario
        }

        // Guardar la notificación con el estado actualizado
        return notificacionRepository.save(notificacion);
    }

    @Override
    public List<Notificacion> listar() {
        List<Notificacion> notificacions = notificacionRepository.findAll();

        for (Notificacion notificacion : notificacions) {
            ClienteDto clienteDto =
                    clienteFeing.obtenerPorId(notificacion.getClienteId()).getBody();
            notificacion.setClienteDto(clienteDto);
        }
        return notificacions;
    }

    @Override
    public Optional<Notificacion> buscar(Integer id) {
        if (!notificacionRepository.existsById(id)) {
            throw new IllegalArgumentException("La notificaion con id " + id +" no existe");
        }
        Optional<Notificacion> optionalNotificacion = notificacionRepository.findById(id);

        optionalNotificacion.ifPresent(notificacion -> {
            ClienteDto clienteDto = clienteFeing.obtenerPorId(notificacion.getClienteId()).getBody();
            notificacion.setClienteDto(clienteDto);

        });
        return optionalNotificacion;
    }

    @Override
    public Notificacion guardar(Notificacion notificacion) {
        ClienteDto clienteDto = clienteFeing.obtenerPorId(notificacion.getNotificacionId()).getBody();
        if (clienteDto == null) {
            throw new RuntimeException("Cliente no encontrado");
        }

        notificacion.setEmail(clienteDto.getEmail());

        if (notificacion.getNotificacionId() !=
                null && notificacionRepository.existsById(notificacion.getNotificacionId())){
            throw new DataIntegrityViolationException("Ya existe una Notificacion con ese RUC/DNI");
        }

        return notificacionRepository.save(notificacion);
    }


    @Override
    public Notificacion actualizar(Integer id, String estado) {
        if (!notificacionRepository.existsById(id)){
            throw new IllegalArgumentException("Notificacion con referencia "+id+" no existe");
        }

         Notificacion notificacion =
                 notificacionRepository.findById(id).orElseThrow(() ->
                         new IllegalArgumentException("Notificación con referencia " + id + " no existe"));


        notificacion.setEstado(estado);

        return notificacionRepository.save(notificacion);
    }

}
