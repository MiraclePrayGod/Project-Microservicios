package contacloud.dppagos.service;

import contacloud.dppagos.entity.Pagos;

import java.util.List;
import java.util.Optional;

public interface PagosService {
    List<Pagos> listar();
    Optional<Pagos> buscar(Integer id);
    Pagos guardar(Pagos pago);
    Pagos actualizar(Integer id , Pagos pago);
    void  eliminar (Integer id);

}
