package com.ilich.bt.backend.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "valores_historicos")
public class ValorHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "criptomoneda_id")
    private Criptomoneda criptomoneda;

    @ManyToOne
    @JoinColumn(name = "moneda_id")
    private Moneda moneda;

    private Double valor; // Ej: 1 BTC = 27,000 USD

    private LocalDateTime fecha; // Fecha y hora del valor
}
