package com.ilich.bt.backend.controller;

import com.ilich.bt.backend.dto.request.CriptomonedaRequest;
import com.ilich.bt.backend.dto.request.CriptomonedaValorRequest;
import com.ilich.bt.backend.entity.Criptomoneda;
import com.ilich.bt.backend.service.CriptomonedaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/criptomonedas")
@RequiredArgsConstructor
public class CriptomonedaController {

    @Autowired
    private CriptomonedaService criptomonedaService;

    @PostMapping
    public ResponseEntity<Criptomoneda> crearOActualizar(@RequestBody CriptomonedaRequest request) {
        Criptomoneda nueva = criptomonedaService.crearOActualizar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Criptomoneda> actualizar(
            @PathVariable Long id,
            @RequestBody CriptomonedaValorRequest request) {

        Criptomoneda actualizada = criptomonedaService.actualizar(id, request);
        return ResponseEntity.ok(actualizada);
    }
}
