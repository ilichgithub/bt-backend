package com.ilich.bt.backend.controller;

import com.ilich.bt.backend.entity.Moneda;
import com.ilich.bt.backend.service.MonedaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/moneda")
@RequiredArgsConstructor
public class MonedaController {

    @Autowired
    private MonedaService monedaService;

    @GetMapping
    public List<Moneda> listarMonedas() {
        return monedaService.listarTodas();
    }

    @PostMapping
    public ResponseEntity<Moneda> crearMoneda(@RequestBody Moneda moneda) {
        Moneda nueva = monedaService.crear(moneda);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }
}