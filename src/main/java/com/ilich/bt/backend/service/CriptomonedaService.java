package com.ilich.bt.backend.service;

import com.ilich.bt.backend.dto.CriptomonedaMonedaDTO;
import com.ilich.bt.backend.dto.MonedaConValorDTO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CriptomonedaService {
    private static final Logger log = LoggerFactory.getLogger(CriptomonedaService.class);

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
    public List<CriptomonedaMonedaDTO> listarTodas() {
        log.info("Buscando criptomonedas relacionadas con moneda listarTodas");
        List<Criptomoneda> criptos = criptomonedaRepository.findAll();

        return criptos.stream()
                .map(cripto -> {
                    List<MonedaConValorDTO> monedaDTOs = cripto.getMonedas().stream()
                            .map(moneda -> {
                                Optional<ValorHistorico> ultimo = valorHistoricoRepository
                                        .findFirstByCriptomonedaIdAndMonedaIdOrderByFechaDesc(
                                                cripto.getId(), moneda.getId()
                                        );

                                return new MonedaConValorDTO(
                                        moneda.getId(),
                                        moneda.getNombre(),
                                        moneda.getCodigo(),
                                        moneda.getSimbolo(),
                                        ultimo.map(ValorHistorico::getValor).orElse(null),
                                        ultimo.map(ValorHistorico::getFecha).orElse(null)
                                );
                            })
                            .toList();

                    return new CriptomonedaMonedaDTO(
                            cripto.getId(),
                            cripto.getNombre(),
                            cripto.getCodigo(),
                            monedaDTOs
                    );
                })
                .toList();
    }


    public List<CriptomonedaMonedaDTO> listarPorMoneda(String codigoMoneda) {
        log.info("Buscando criptomonedas relacionadas con moneda listarPorMoneda");
        List<Criptomoneda> criptos = criptomonedaRepository.findByMonedaCodigo(codigoMoneda);

        return criptos.stream()
                .map(c -> {
                    Moneda monedaFiltrada = c.getMonedas().stream()
                            .filter(m -> m.getCodigo().equalsIgnoreCase(codigoMoneda))
                            .findFirst()
                            .orElse(null);

                    Optional<ValorHistorico> ultimo = valorHistoricoRepository
                            .findFirstByCriptomonedaIdAndMonedaIdOrderByFechaDesc(
                                    c.getId(), monedaFiltrada.getId()
                            );

                    MonedaConValorDTO monedaDTO = new MonedaConValorDTO(
                            monedaFiltrada.getId(),
                            monedaFiltrada.getNombre(),
                            monedaFiltrada.getCodigo(),
                            monedaFiltrada.getSimbolo(),
                            ultimo.map(ValorHistorico::getValor).orElse(null),
                            ultimo.map(ValorHistorico::getFecha).orElse(null)
                    );

                    return new CriptomonedaMonedaDTO(
                            c.getId(),
                            c.getNombre(),
                            c.getCodigo(),
                            List.of(monedaDTO)
                    );
                })
                .toList();
    }


}
