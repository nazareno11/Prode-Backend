package com.prog4_tpi_grupo1.backend.partido.service;

import com.prog4_tpi_grupo1.backend.partido.dto.ResultadoPartidoDTO;
import com.prog4_tpi_grupo1.backend.partido.entity.EstadoPartido;
import com.prog4_tpi_grupo1.backend.partido.entity.Partido;
import com.prog4_tpi_grupo1.backend.partido.entity.Tendencia;
import com.prog4_tpi_grupo1.backend.partido.repository.PartidoRepository;
import com.prog4_tpi_grupo1.backend.shared.config.esceptions.NotFoundException;
import com.prog4_tpi_grupo1.backend.shared.config.esceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartidoService {

    private final PartidoRepository partidoRepository;

    public Partido iniciarPartido(Long partidoId) {

        Partido partido = partidoRepository.findById(partidoId)
                .orElseThrow(() -> new NotFoundException("Partido no encontrado"));

        partido.setEstado(EstadoPartido.EN_JUEGO);

        return partidoRepository.save(partido);
    }

    public Partido cargarResultado(Long partidoId,
                                   ResultadoPartidoDTO dto) {

        Partido partido = partidoRepository.findById(partidoId)
                .orElseThrow(() -> new NotFoundException("Partido no encontrado"));

        if(partido.getEstado() != EstadoPartido.EN_JUEGO) {
            throw new BadRequestException(
                    "El partido debe estar EN_JUEGO");
        }

        partido.setResultadoLocal(dto.getResultadoLocal());
        partido.setResultadoVisitante(dto.getResultadoVisitante());

        partido.setTendencia(
                calcularTendencia(
                        dto.getResultadoLocal(),
                        dto.getResultadoVisitante()
                )
        );

        partido.setEstado(EstadoPartido.FINALIZADO);

        return partidoRepository.save(partido);
    }

    private Tendencia calcularTendencia(Integer local,
                                        Integer visitante) {

        if(local > visitante) {
            return Tendencia.LOCAL;
        }

        if(local < visitante) {
            return Tendencia.VISITANTE;
        }

        return Tendencia.EMPATE;
    }
}