package com.ilich.bt.backend.dto;

import com.ilich.bt.backend.entity.Moneda;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CriptomonedaMonedaDTO {
    private Long id;
    private String nombre;
    private String codigo;
    private List<MonedaConValorDTO> monedas;
}
