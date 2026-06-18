package com.prog4_tpi_grupo1.backend.partido.controller;

import com.prog4_tpi_grupo1.backend.partido.dto.PartidoResponseDTO;
import com.prog4_tpi_grupo1.backend.partido.entity.Partido;
import com.prog4_tpi_grupo1.backend.partido.repository.PartidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partidos")
@RequiredArgsConstructor
public class PartidoController {

    private final PartidoRepository partidoRepository;

    @GetMapping
    public List<PartidoResponseDTO> listar(
            @RequestParam(required = false)
            Long fechaId
    ) {

        List<Partido> partidos;

        if (fechaId != null) {

            partidos = partidoRepository
                    .findByFechaIdOrderByFechaHoraAsc(fechaId);

        } else {

            partidos = partidoRepository
                    .findAllByOrderByFechaHoraAsc();
        }

        return partidos.stream()
                .map(this::toDto)
                .toList();
    }

    private PartidoResponseDTO toDto(Partido partido) {

        return PartidoResponseDTO.builder()
                .id(partido.getId())
                .externalId(partido.getExternalId())
                .fechaHora(partido.getFechaHora())
                .estado(partido.getEstado())
                .matchday(partido.getMatchday())
                .grupo(partido.getGrupo())
                .stage(partido.getStage())
                .equipoLocal(partido.getEquipoLocal().getNombre())
                .equipoVisitante(partido.getEquipoVisitante().getNombre())
                .resultadoLocal(partido.getResultadoLocal())
                .resultadoVisitante(partido.getResultadoVisitante())
                .fecha(partido.getFecha().getNombre())
                .build();
    }
}
