package com.ilich.bt.backend.service;

import com.ilich.bt.backend.dto.ValorMonedaDTO;
import com.ilich.bt.backend.dto.request.CriptomonedaRequest;
import com.ilich.bt.backend.dto.request.CriptomonedaValorRequest;
import com.ilich.bt.backend.entity.Criptomoneda;
import com.ilich.bt.backend.entity.Moneda;
import com.ilich.bt.backend.entity.ValorHistorico;
import com.ilich.bt.backend.repository.CriptomonedaRepository;
import com.ilich.bt.backend.repository.MonedaRepository;
import com.ilich.bt.backend.repository.ValorHistoricoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CriptomonedaService {

    @Autowired
    private CriptomonedaRepository criptomonedaRepository;

    @Autowired
    private MonedaRepository monedaRepository;

    @Autowired
    private ValorHistoricoRepository valorHistoricoRepository;

    public Criptomoneda crearOActualizar(CriptomonedaRequest request) {
        Moneda moneda = monedaRepository.findById(request.getMonedaId())
                .orElseThrow(() -> new RuntimeException("Moneda no encontrada"));

        Optional<Criptomoneda> existente = criptomonedaRepository.findByCodigo(request.getCodigo());

        if (existente.isPresent()) {
            Criptomoneda criptomoneda = existente.get();

            if (!criptomoneda.getMonedas().contains(moneda)) {
                criptomoneda.getMonedas().add(moneda);
                return criptomonedaRepository.save(criptomoneda);
            }

            return criptomoneda;
        }

        Criptomoneda nueva = Criptomoneda.builder()
                .nombre(request.getNombre())
                .codigo(request.getCodigo())
                .monedas(new ArrayList<>(List.of(moneda)))
                .build();

        return criptomonedaRepository.save(nueva);
    }

    public Criptomoneda actualizar(Long id, CriptomonedaValorRequest request) {
        Criptomoneda criptomoneda = criptomonedaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Criptomoneda no encontrada"));

        // Validar que todas las monedas enviadas existen
        List<Long> monedaIds = request.getValores().stream()
                .map(ValorMonedaDTO::getMonedaId)
                .toList();

        List<Moneda> monedas = monedaRepository.findAllById(monedaIds);

        if (monedas.size() != monedaIds.size()) {
            throw new RuntimeException("Una o más monedas no existen");
        }

        // Validar que todas las monedas ya relacionadas estén en la entrada
        List<Long> idsRelacionadas = criptomoneda.getMonedas().stream()
                .map(Moneda::getId)
                .toList();

        if (!monedaIds.containsAll(idsRelacionadas)) {
            throw new RuntimeException("Faltan valores para monedas ya relacionadas");
        }

        // Actualizar nombre y código
        criptomoneda.setNombre(request.getNombre());
        criptomoneda.setCodigo(request.getCodigo());

        // Agregar nuevas relaciones si no existen
        for (Moneda moneda : monedas) {
            if (!criptomoneda.getMonedas().contains(moneda)) {
                criptomoneda.getMonedas().add(moneda);
            }
        }

        // Registrar histórico
        for (ValorMonedaDTO dto : request.getValores()) {
            Moneda moneda = monedas.stream()
                    .filter(m -> m.getId().equals(dto.getMonedaId()))
                    .findFirst()
                    .orElseThrow(); // ya validado

            ValorHistorico historico = ValorHistorico.builder()
                    .criptomoneda(criptomoneda)
                    .moneda(moneda)
                    .valor(dto.getValor())
                    .fecha(LocalDateTime.now())
                    .build();

            valorHistoricoRepository.save(historico);
        }

        return criptomonedaRepository.save(criptomoneda);
    }
}
