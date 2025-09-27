package com.ilich.bt.backend.repository;

import com.ilich.bt.backend.entity.Criptomoneda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CriptomonedaRepository extends JpaRepository<Criptomoneda, Long> {
    Optional<Criptomoneda> findByCodigo(String codigo);

    @Query("SELECT c FROM Criptomoneda c JOIN c.monedas m WHERE m.codigo = :codigo")
    List<Criptomoneda> findByMonedaCodigo(@Param("codigo") String codigo);

}
