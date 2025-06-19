package contacloud.dplicencias.service.impl;

import contacloud.dplicencias.dto.*;
import contacloud.dplicencias.entity.Licencia;
import contacloud.dplicencias.entity.LicenciaDetalle;
import contacloud.dplicencias.feign.ClienteFeing;
import contacloud.dplicencias.feign.ProductoFeing;
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
    private ProductoFeing productoFeing;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private JavaMailSenderImpl mailSender;



    // Método listar sin CircuitBreaker
    @Override
    public List<Licencia> listar() {
        List<Licencia> licencias = licenciaRepository.findAll();
        for (Licencia licencia : licencias) {
            ClienteDto clienteDto = clienteFeing.obtenerPorId(licencia.getClienteId()).getBody();
            licencia.setClienteDto(clienteDto);

            for (LicenciaDetalle detalle: licencia.getDetalles()) {
                VentaDto ventaDto = ventaFeing.obtenerPorId(detalle.getVentaId()).getBody();
                detalle.setVentaDto(ventaDto);
                ProductoDto productoDto = productoFeing.obtenerProductoPorId(detalle.getProductoId()).getBody();
                detalle.setProductoDato(productoDto);
            }
        }
        return licencias;
    }

    // Método buscar sin CircuitBreaker
    @Override
    public Optional<Licencia> buscar(Integer id) {
//        Optional<Licencia> optionalLicencia = licenciaRepository.findById(id);

//        optionalLicencia.ifPresent(licencia -> {
//            ClienteDto clienteDto = clienteFeing.obtenerPorId(licencia.getClienteId()).getBody();
//            licencia.setClienteDto(clienteDto);
//            for (LicenciaDetalle detalle: licencia.getDetalles()) {
//                VentaDto ventaDto = ventaFeing.obtenerPorId(detalle.getVentaId()).getBody();
//                detalle.setVentaDto(ventaDto);
//            }
//        });
//
//        return optionalLicencia;
        return licenciaRepository.findById(id);
    }

    // Método guardar sin CircuitBreaker
    @Override
    public Licencia guardar(Licencia licenciaDato) {
//        ClienteDto cliente = clienteFeing.obtenerPorId(licenciaDato.getClienteId()).getBody();
//        if (cliente == null) {
//            throw new RuntimeException("Cliente no encontrado con ID: " + licenciaDato.getClienteId());
//        }
//        Licencia licencia = new Licencia();
//        licencia.setClienteId(cliente.getId());
//        licencia.setTipoLicencia(licenciaDato.getTipoLicencia());
//        licencia.setFechaExpiracion(licenciaDato.getFechaExpiracion());
//        licencia.setEstado(licenciaDato.getEstado());
//
//        List<LicenciaDetalle> detalles = new ArrayList<>();
//
//        for (LicenciaDetalleCreateDto detalleDto : licenciaDato.getDetalles()) {
//            VentaDto ventaDto = ventaFeing.obtenerPorId(detalleDto.getVentaId()).getBody();
//            if (ventaDto == null || !"PAGADO".equalsIgnoreCase(ventaDto.getEstado())) {
//                throw new RuntimeException("Venta no válida o no completada, ID: "
//                        + detalleDto.getVentaId());
//            }
//            LicenciaDetalle detalle = new LicenciaDetalle();
//            detalle.setVentaId(ventaDto.getId());
//            detalle.setCodigoLicencia(generarCodigoLicencia(cliente.getNombre()));
//            detalle.setContrasena(generarContrasena(cliente.getNombre()));
//
//            detalles.add(detalle);
//        }
//        licencia.setDetalles(detalles);
        return licenciaRepository.save(licenciaDato);
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
        return "";
    }
}
