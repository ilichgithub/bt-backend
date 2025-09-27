package com.ilich.bt.backend.dto.request;

import com.ilich.bt.backend.dto.ValorMonedaDTO;
import lombok.Data;

import java.util.List;

@Data
public class CriptomonedaValorRequest {
    private String nombre;
    private String codigo;
    private List<ValorMonedaDTO> valores;
}
