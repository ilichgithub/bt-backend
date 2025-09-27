package com.ilich.bt.backend.repository;

import com.ilich.bt.backend.entity.ValorHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValorHistoricoRepository extends JpaRepository<ValorHistorico, Long> {
}
