package com.ilich.bt.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MonedaConValorDTO {
    private Long id;
    private String nombre;
    private String codigo;
    private String simbolo;
    private Double ultimoValor;
    private LocalDateTime fecha;
}
