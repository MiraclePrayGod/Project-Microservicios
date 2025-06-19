package contacloud.dplicencias.util;

import contacloud.dplicencias.dto.ClienteDto;
import contacloud.dplicencias.entity.Licencia;
import contacloud.dplicencias.entity.LicenciaDetalle;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

public class StringUtils {

    private String generarCodigoLicencia(String nombreCliente) {
        String nombreBase = nombreCliente.length() >= 3 ? nombreCliente.substring(0, 3).toUpperCase() : nombreCliente.toUpperCase();
        SecureRandom random = new SecureRandom();
        int numeroAleatorio = random.nextInt(100000);
        return nombreBase + String.format("%05d", numeroAleatorio);
    }

    private String generarContrasena(String nombreCliente) {
        String base = nombreCliente.substring(0, Math.min(nombreCliente.length(), 4)).toLowerCase();
        SecureRandom random = new SecureRandom();
        StringBuilder contrasena = new StringBuilder(base);
        for (int i = 0; i < 4; i++) {
            contrasena.append((char) (random.nextInt(26) + 'a'));
        }
        return contrasena.toString();
    }

//    @Override
//    public String sendEmail(Integer clienteId) {
//        ClienteDto clienteDto = clienteFeing.obtenerPorId(clienteId).getBody();
//        String email = clienteDto.getEmail();
//
//        if (email == null || email.isEmpty()) {
//            throw new IllegalArgumentException("El cliente no tiene un correo electrónico válido.");
//        }
//
//        try {
//            String codigo = "NO DISPONIBLE";
//            String contrasena = "NO DISPONIBLE";
//            String nombreCliente = clienteDto.getNombre();
//
//            List<Licencia> licencias = licenciaRepository.findByClienteId(clienteId);
//
//            if (!licencias.isEmpty()) {
//                for (Licencia licencia : licencias) {
//                    if (licencia.getDetalles() != null && !licencia.getDetalles().isEmpty()) {
//                        LicenciaDetalle licenciaDetalle = licencia.getDetalles().get(0);
//                        codigo = licenciaDetalle.getCodigoLicencia();
//                        contrasena = licenciaDetalle.getContrasena();
//                    } else {
//                        throw new RuntimeException("Licencia no tiene detalles asociados.");
//                    }
//
//                    Context context = new Context();
//                    context.setVariable("codigo", codigo);
//                    context.setVariable("contrasena", contrasena);
//                    context.setVariable("cliente", nombreCliente);
//
//                    String htmlContent = templateEngine.process("email/licencia-activa", context);
//
//                    MimeMessage mimeMessage = mailSender.createMimeMessage();
//                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//
//                    helper.setFrom("reginaldomayhuire@upeu.edu.pe");
//                    helper.setTo(email);
//                    helper.setSubject("LICENCIA DE SOFTWARE");
//                    helper.setText(htmlContent, true);
//
//                    mailSender.send(mimeMessage);
//                }
//
//                return UUID.randomUUID().toString();
//            } else {
//                throw new RuntimeException("No se encontraron licencias para el cliente con ID: " + clienteId);
//            }
//
//        } catch (MessagingException e) {
//            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
//        }
//    }
}
