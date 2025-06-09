package contacloud.dplicencias.service.impl;

import contacloud.dplicencias.dto.ClienteDto;
import contacloud.dplicencias.dto.LicenciaCreateDto;
import contacloud.dplicencias.dto.LicenciaDetalleCreateDto;
import contacloud.dplicencias.dto.VentaDto;
import contacloud.dplicencias.entity.Licencia;
import contacloud.dplicencias.entity.LicenciaDetalle;
import contacloud.dplicencias.feign.ClienteFeing;
import contacloud.dplicencias.feign.VentaFeing;
import contacloud.dplicencias.repository.LicenciaRepository;
import contacloud.dplicencias.service.LicenciaService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LicenciaSeriveImpl implements LicenciaService {

    @Autowired
    private LicenciaRepository licenciaRepository;

    @Autowired
    private ClienteFeing clienteFeing;

    @Autowired
    private VentaFeing ventaFeing;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private JavaMailSenderImpl mailSender;


    private String generarCodigoLicencia(String nombreCliente) {
        // Tomamos las primeras 3 letras del nombre del cliente (puedes ajustar esto según el caso)
        String nombreBase = nombreCliente.length() >= 3 ? nombreCliente.substring(0, 3).toUpperCase() : nombreCliente.toUpperCase();

        // Generar una parte aleatoria del código (números)
        SecureRandom random = new SecureRandom();
        int numeroAleatorio = random.nextInt(100000);  // Un número aleatorio de 5 dígitos

        // Concatenamos las letras del nombre con el número aleatorio
        return nombreBase + String.format("%05d", numeroAleatorio);  // Código con el nombre + 5 dígitos aleatorios
    }

    private String generarContrasena(String nombreCliente) {
        // Toma una parte del nombre del cliente y la usa como base para la contraseña
        String base = nombreCliente.substring(0, Math.min(nombreCliente.length(), 4)).toLowerCase(); // Toma las primeras 4 letras del nombre

        // Genera una contraseña aleatoria con los caracteres del nombre y otros aleatorios
        SecureRandom random = new SecureRandom();
        StringBuilder contrasena = new StringBuilder(base);

        // Añadir caracteres aleatorios a la contraseña
        for (int i = 0; i < 4; i++) {
            contrasena.append((char) (random.nextInt(26) + 'a'));  // Genera caracteres aleatorios
        }

        return contrasena.toString();
    }


    @Override
    public List<Licencia> listar() {
        List<Licencia> licencias = licenciaRepository.findAll();
        for (Licencia licencia : licencias) {
            ClienteDto clienteDto = clienteFeing.obtenerPorId(licencia.getClienteId()).getBody();
            licencia.setClienteDto(clienteDto);

            for (LicenciaDetalle detalle: licencia.getDetalles()) {
                VentaDto ventaDto = ventaFeing.obtenerPorId(detalle.getVentaId()).getBody();
                detalle.setVentaDto(ventaDto);
            }
        }
        return licencias;
    }

    @Override
    public Optional<Licencia> buscar(Integer id) {
        Optional<Licencia> optionallicencias = licenciaRepository.findById(id);

        optionallicencias.ifPresent(licencia -> {
            ClienteDto clienteDto = clienteFeing.obtenerPorId(licencia.getClienteId()).getBody();
            licencia.setClienteDto(clienteDto);
        for (LicenciaDetalle detalle: licencia.getDetalles()) {
                VentaDto ventaDto = ventaFeing.obtenerPorId(detalle.getVentaId()).getBody();
                detalle.setVentaDto(ventaDto);
        }
        });
        return optionallicencias;
    }

    @Override
    public Licencia guardar(LicenciaCreateDto licenciaDato) {
        ClienteDto cliente = clienteFeing.obtenerPorId(licenciaDato.getClienteId()).getBody();
        if (cliente == null) {
            throw new RuntimeException("Cliente no encontrado con ID: " + licenciaDato.getClienteId());
        }
        Licencia licencia = new Licencia();
        licencia.setClienteId(cliente.getId());
        licencia.setTipoLicencia(licenciaDato.getTipoLicencia());
        licencia.setFechaExpiracion(licenciaDato.getFechaExpiracion());
        licencia.setEstado(licenciaDato.getEstado());

        List<LicenciaDetalle> detalles = new ArrayList<>();

        for (LicenciaDetalleCreateDto detalleDto : licenciaDato.getDetalles()) {
            VentaDto ventaDto = ventaFeing.obtenerPorId(detalleDto.getVentaId()).getBody();
            if (ventaDto == null || !"COMPLETA".equalsIgnoreCase(ventaDto.getEstado())) {
                throw new RuntimeException("Venta no válida o no completada, ID: "
                        + detalleDto.getVentaId());
            }
            LicenciaDetalle detalle = new LicenciaDetalle();
            detalle.setVentaId(ventaDto.getId());
            detalle.setCodigoLicencia(generarCodigoLicencia(cliente.getNombre()));
            detalle.setContrasena(generarContrasena(cliente.getNombre()));

            detalles.add(detalle);
        }
        licencia.setDetalles(detalles);
        return licenciaRepository.save(licencia);
    }

    @Override
    public Licencia actualizar(Integer id, Licencia licencia) {
        Licencia licenciaExistente = licenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Licencia no encontrada con ID: " + id));

        // Actualizar los campos de la licencia existente
    return licenciaRepository.save(licencia);
    }

    @Override
    public void eliminar(Integer id) {
    licenciaRepository.deleteById(id);
    }

    @Override
    public String sendEmail(Integer clienteId) {

        ClienteDto clienteDto = clienteFeing.obtenerPorId(clienteId).getBody();
        String email = clienteDto.getEmail();

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("El cliente no tiene un correo electrónico válido.");
        }

        try {

            String codigo = "NO DISPONIBLE";
            String contrasena = "NO DISPONIBLE";
            String nombreCliente = clienteDto.getNombre(); // O usa .getNombres() o similar, según tu DTO

            // Recuperar todas las licencias asociadas al cliente
            List<Licencia> licencias = licenciaRepository.findByClienteId((clienteId)); // Cambié a findByClienteId para obtener todas las licencias

            if (!licencias.isEmpty()) {
                // Iterar sobre todas las licencias
                for (Licencia licencia : licencias) {
                    // Comprobar si la licencia tiene detalles
                    if (licencia.getDetalles() != null && !licencia.getDetalles().isEmpty()) {
                        // Obtener el primer detalle de la licencia (puedes modificar si deseas iterar por todos los detalles)
                        LicenciaDetalle licenciaDetalle = licencia.getDetalles().get(0);

                        // Extraer los valores de codigoLicencia y contrasena
                        codigo = licenciaDetalle.getCodigoLicencia();
                        contrasena = licenciaDetalle.getContrasena();
                    } else {
                        throw new RuntimeException("Licencia no tiene detalles asociados.");
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
                }

                return UUID.randomUUID().toString(); // Puedes cambiar esto si deseas retornar algo más

            } else {
                throw new RuntimeException("No se encontraron licencias para el cliente con ID: " + clienteId);
            }

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
        }

    }



}
