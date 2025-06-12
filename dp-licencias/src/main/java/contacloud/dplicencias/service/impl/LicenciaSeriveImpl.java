package contacloud.dplicencias.service.impl;

import contacloud.dplicencias.dto.*;
import contacloud.dplicencias.entity.Licencia;
import contacloud.dplicencias.entity.LicenciaDetalle;
import contacloud.dplicencias.feign.ClienteFeing;
import contacloud.dplicencias.feign.ProductoFeing;
import contacloud.dplicencias.feign.VentaFeing;
import contacloud.dplicencias.repository.LicenciaRepository;
import contacloud.dplicencias.service.LicenciaService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    private ProductoFeing productoFeing;


    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private JavaMailSenderImpl mailSender;


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

    // Método listar con CircuitBreaker
    @Override
    @CircuitBreaker(name = "licenciaListarCB", fallbackMethod = "fallBackMethodListar")
    public List<Licencia> listar() {
        List<Licencia> licencias = licenciaRepository.findAll();
        for (Licencia licencia : licencias) {
            ClienteDto clienteDto = clienteFeing.obtenerPorId(licencia.getClienteId()).getBody();
            licencia.setClienteDto(clienteDto);

            for (LicenciaDetalle detalle: licencia.getDetalles()) {
                ProductoDto productoDto = productoFeing.obtenerPorId(detalle.getProductoId()).getBody();
                VentaDto ventaDto = ventaFeing.obtenerPorId(detalle.getVentaId()).getBody();
                detalle.setVentaDto(ventaDto);
                detalle.setProductoDato(productoDto);
            }
        }
        return licencias;
    }



    // Método buscar con CircuitBreaker
    @Override
    @CircuitBreaker(name = "licenciaBuscarCB", fallbackMethod = "fallBackMethodBuscar")
    public Optional<Licencia> buscar(Integer id) {
        Optional<Licencia> optionalLicencia = licenciaRepository.findById(id);

        optionalLicencia.ifPresent(licencia -> {
            ClienteDto clienteDto = clienteFeing.obtenerPorId(licencia.getClienteId()).getBody();
            licencia.setClienteDto(clienteDto);
            for (LicenciaDetalle detalle: licencia.getDetalles()) {
                ProductoDto productoDto = productoFeing.obtenerPorId(detalle.getProductoId()).getBody();
                VentaDto ventaDto = ventaFeing.obtenerPorId(detalle.getVentaId()).getBody();
                detalle.setVentaDto(ventaDto);
                detalle.setProductoDato(productoDto);
            }
        });

        return optionalLicencia;
    }

    // Método fallback para buscar
    public Optional<Licencia> fallBackMethodBuscar(Integer id, Throwable t) {
        System.err.println("🚨 Fallback buscarLicencia activado para id " + id + ": " + t.getMessage());
        Licencia fallback = new Licencia();
        fallback.setId(-1);
        fallback.setClienteDto(new ClienteDto());
        return Optional.of(fallback);
    }

    // Método guardar con CircuitBreaker
    @Override
    @CircuitBreaker(name = "licenciaGuardarCB", fallbackMethod = "fallBackMethodGuardar")
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
            if (ventaDto == null || !"PAGADO".equalsIgnoreCase(ventaDto.getEstado())) {
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

    // Método fallback para guardar
    public Licencia fallBackMethodGuardar(LicenciaCreateDto licenciaDato, Throwable t) {
        System.err.println("🚨 Fallback guardarLicencia activado: " + t.getMessage());
        Licencia fallback = new Licencia();
        fallback.setId(-1);
        return fallback;
    }

    @Override
    public Licencia actualizar(Integer id, Licencia licencia) {
        Licencia licenciaExistente = licenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Licencia no encontrada con ID: " + id));
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
            String nombreCliente = clienteDto.getNombre();

            List<Licencia> licencias = licenciaRepository.findByClienteId(clienteId);

            if (!licencias.isEmpty()) {
                for (Licencia licencia : licencias) {
                    if (licencia.getDetalles() != null && !licencia.getDetalles().isEmpty()) {
                        LicenciaDetalle licenciaDetalle = licencia.getDetalles().get(0);
                        codigo = licenciaDetalle.getCodigoLicencia();
                        contrasena = licenciaDetalle.getContrasena();
                    } else {
                        throw new RuntimeException("Licencia no tiene detalles asociados.");
                    }

                    Context context = new Context();
                    context.setVariable("codigo", codigo);
                    context.setVariable("contrasena", contrasena);
                    context.setVariable("cliente", nombreCliente);

                    String htmlContent = templateEngine.process("email/licencia-activa", context);

                    MimeMessage mimeMessage = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                    helper.setFrom("reginaldomayhuire@upeu.edu.pe");
                    helper.setTo(email);
                    helper.setSubject("LICENCIA DE SOFTWARE");
                    helper.setText(htmlContent, true);

                    mailSender.send(mimeMessage);
                }

                return UUID.randomUUID().toString();
            } else {
                throw new RuntimeException("No se encontraron licencias para el cliente con ID: " + clienteId);
            }

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
        }
    }
}
