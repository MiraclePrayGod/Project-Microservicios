package contacloud.dpnotificacion.service.impl;

import contacloud.dpnotificacion.dto.ClienteDto;
import contacloud.dpnotificacion.dto.LicenciaDato;
import contacloud.dpnotificacion.dto.LicenciaDetalleNotificacionDto;
import contacloud.dpnotificacion.entity.Notificacion;
import contacloud.dpnotificacion.feing.ClienteFeing;
import contacloud.dpnotificacion.feing.LicenciaFeign;
import contacloud.dpnotificacion.repository.NotificacionRepository;
import contacloud.dpnotificacion.service.NotificacionService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class NotificacionServiceImpl implements NotificacionService {

    // Constructor para la inyección de dependencias

    @Autowired
    private NotificacionRepository notificacionRepository;
    @Autowired
    private ClienteFeing clienteFeing;
    @Autowired
    private  JavaMailSender mailSender;
    @Autowired
    private LicenciaFeign licenciaFeign;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public List<Notificacion> listar() {
        List<Notificacion> notificacions = notificacionRepository.findAll();

        for (Notificacion notificacion : notificacions) {
            ClienteDto clienteDto =
                    clienteFeing.obtenerPorId(notificacion.getClienteId()).getBody();
            LicenciaDato licenciaDato = licenciaFeign.obtenerPorId(notificacion.getLicenciaId()).getBody();

            notificacion.setLicenciaDto(licenciaDato);
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
        // Validar cliente
        ClienteDto clienteDto = clienteFeing.obtenerPorId(notificacion.getClienteId()).getBody();
        if (clienteDto == null) {
            throw new RuntimeException("Cliente no encontrado con ID: " + notificacion.getClienteId());
        }

        // Validar licencia y obtener detalles
        LicenciaDato licencia = licenciaFeign.obtenerPorId(notificacion.getLicenciaId()).getBody();

        if (licencia != null && Boolean.TRUE.equals(licencia.getEstado())) {
            for (LicenciaDetalleNotificacionDto detalle : licencia.getDetalles()) {
                System.out.println("Código: " + detalle.getCodigoLicencia());
                System.out.println("Contraseña: " + detalle.getContrasena());

                // Aquí podrías construir el mensaje, asunto, etc.
                // Ejemplo:
                String mensaje = "Estimado cliente,\nSu licencia está activa.\n" +
                        "Código: " + detalle.getCodigoLicencia() + "\n" +
                        "Contraseña: " + detalle.getContrasena();

                notificacion.setMensaje(mensaje);
                notificacion.setAsunto("Detalles de su Licencia");
                notificacion.setFechaEnvio(LocalDateTime.now());
                notificacion.setEstado("pendiente");
            }
        } else {
            throw new RuntimeException("Licencia no válida o inactiva");
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

        try {
            LicenciaDato licenciaDto =
                    licenciaFeign.obtenerPorId(notificacion.getLicenciaId()).getBody();
            notificacion.setLicenciaDto(licenciaDto); // Asegura que lo tenga si luego se guarda
            ClienteDto clienteDto1 = clienteFeing.obtenerPorId(clienteId).getBody();
            notificacion.setClienteDto(clienteDto1);
            String codigo = "NO DISPONIBLE";
            String contrasena = "NO DISPONIBLE";
            String nombreCliente = clienteDto.getNombre(); // O usa .getNombres() o similar, según tu DTO

            if (licenciaDto != null && licenciaDto.getDetalles() != null && !licenciaDto.getDetalles().isEmpty()) {
                LicenciaDetalleNotificacionDto detalle = licenciaDto.getDetalles().get(0); // puedes usar el primero
                codigo = detalle.getCodigoLicencia();
                contrasena = detalle.getContrasena();
            }

            // Crear contexto Thymeleaf
            Context context = new Context();
            context.setVariable("codigo", codigo);
            context.setVariable("contrasena", contrasena);
            context.setVariable("cliente", nombreCliente);
            // Procesar plantilla
            String htmlContent = templateEngine.process("email/licencia-activa", context);

            // Construir y enviar el correo HTML
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("reginaldomayhuire@upeu.edu.pe");
            helper.setTo(email);
            helper.setSubject("LICENCIA DE SOFTWARE");
            helper.setText(htmlContent, true); // true indica HTML

            mailSender.send(mimeMessage);

            // Actualiza la notificación
            notificacion.setEstado("enviado");
            notificacion.setFechaEnvio(LocalDateTime.now());
            notificacionRepository.save(notificacion);

            return UUID.randomUUID().toString();

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
        }

    }

}
