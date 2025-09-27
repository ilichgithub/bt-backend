package com.ilich.bt.backend.controller;

import com.ilich.bt.backend.entity.Criptomoneda;
import com.ilich.bt.backend.service.CriptomonedaService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/criptomoneda")
@RequiredArgsConstructor
public class CriptoController {

    @Autowired
    private CriptomonedaService criptomonedaService;
    private static final Logger log = LoggerFactory.getLogger(CriptoController.class);


    @GetMapping
    public ResponseEntity<List<?>> listar(
            @RequestParam(required = false) String moneda) {

        log.info("Buscando criptomonedas relacionadas con moneda: {}", moneda);

        return ResponseEntity.ok((moneda == null)
                ? criptomonedaService.listarTodas()
                : criptomonedaService.listarPorMoneda(moneda));
    }
}