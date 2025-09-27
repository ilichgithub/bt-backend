package com.ilich.bt.backend.repository;

import com.ilich.bt.backend.entity.Criptomoneda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CriptomonedaRepository extends JpaRepository<Criptomoneda, Long> {
    Optional<Criptomoneda> findByCodigo(String codigo);

}
