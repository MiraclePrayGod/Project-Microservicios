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

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class NotificacionServiceImpl implements NotificacionService {

    // Constructor para la inyección de dependencias
    @Autowired
    private  JavaMailSender javaMailSender;

    @Autowired
    private NotificacionRepository notificacionRepository;
    @Autowired
    private ClienteFeing clienteFeing;
    @Autowired
    private  JavaMailSender mailSender;

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

        clienteDto.setEmail(clienteDto.getEmail());
        clienteDto.setEstado(clienteDto.getEstado());

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

    @Override
    public String sendEmail(Integer notificacionId, Integer clienteId) {
        Notificacion notificacion = notificacionRepository.findById(notificacionId).orElseThrow(() ->
                new IllegalArgumentException("Notificacion con referencia " + notificacionId + " no existe"));

        // Obtener el email y mensaje desde la notificación guardada

        ClienteDto clienteDto = clienteFeing.obtenerPorId(clienteId).getBody();

        String email = clienteDto.getEmail();
        String messageEmail = notificacion.getMensaje();

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("El cliente no tiene un correo electrónico válido.");
        }

            SimpleMailMessage mimeMessage = new SimpleMailMessage();
            mimeMessage.setFrom("reginaldomayhuire@upeu.edu.pe");
            mimeMessage.setTo(email);
            mimeMessage.setText(messageEmail);
            mimeMessage.setSubject("Prueba de correo");
            mailSender.send(mimeMessage);
        String token = UUID.randomUUID().toString();
        return token;
    }

}
