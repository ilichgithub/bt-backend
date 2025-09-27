package com.ilich.bt.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "criptomonedas")
public class Criptomoneda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;     // Ej: "Bitcoin"
    private String codigo;     // Ej: "BTC"

    @ManyToMany
    @JoinTable(
            name = "criptomoneda_moneda",
            joinColumns = @JoinColumn(name = "criptomoneda_id"),
            inverseJoinColumns = @JoinColumn(name = "moneda_id")
    )
    private List<Moneda> monedas;

}
