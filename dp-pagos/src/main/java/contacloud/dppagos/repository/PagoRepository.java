package contacloud.dppagos.repository;


import contacloud.dppagos.entity.Pagos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pagos,Integer> {

    boolean existsByReferencia(String referencia);
}
