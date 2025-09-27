package com.ilich.bt.backend.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CriptomonedaRequest {
    private String nombre;
    private String codigo;
    private Long monedaId;
}
