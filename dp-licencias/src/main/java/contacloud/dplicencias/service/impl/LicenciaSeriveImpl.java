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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LicenciaSeriveImpl implements LicenciaService {

    @Autowired
    private LicenciaRepository licenciaRepository;

    @Autowired
    private ClienteFeing clienteFeing;

    @Autowired
    private VentaFeing ventaFeing;


    private String generarCodigoLicencia() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1000000));  // Código de 6 dígitos
    }

    // Método para generar una contraseña aleatoria
    private String generarContrasena() {
        SecureRandom random = new SecureRandom();
        StringBuilder contrasena = new StringBuilder();
        for (int i = 0; i < 8; i++) {
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
            detalle.setCodigoLicencia(generarCodigoLicencia());
            detalle.setContrasena(generarContrasena());

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

}
