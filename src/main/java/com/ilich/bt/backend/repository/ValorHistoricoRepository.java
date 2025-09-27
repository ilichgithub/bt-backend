package com.ilich.bt.backend.repository;

import com.ilich.bt.backend.entity.ValorHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValorHistoricoRepository extends JpaRepository<ValorHistorico, Long> {
    Optional<ValorHistorico> findFirstByCriptomonedaIdAndMonedaIdOrderByFechaDesc(Long criptomonedaId, Long monedaId);
}
