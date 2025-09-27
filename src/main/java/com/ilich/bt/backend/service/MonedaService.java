package com.ilich.bt.backend.service;

import com.ilich.bt.backend.entity.Moneda;
import com.ilich.bt.backend.repository.MonedaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonedaService {

    @Autowired
    private MonedaRepository monedaRepository;

    public List<Moneda> listarTodas() {
        return monedaRepository.findAll();
    }

    public Moneda crear(Moneda moneda) {
        return monedaRepository.save(moneda);
    }
}