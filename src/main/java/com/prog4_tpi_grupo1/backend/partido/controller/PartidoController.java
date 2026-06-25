package com.prog4_tpi_grupo1.backend.partido.controller;

import com.prog4_tpi_grupo1.backend.partido.dto.PartidoResponseDTO;
import com.prog4_tpi_grupo1.backend.partido.entity.Partido;
import com.prog4_tpi_grupo1.backend.partido.repository.PartidoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Partidos", description = "Consulta de partidos")
@RestController
@RequestMapping("/api/partidos")
@RequiredArgsConstructor
public class PartidoController {

        private final PartidoRepository partidoRepository;

        @Operation(summary = "Listar partidos", description = "Obtiene todos los partidos o filtra por fecha")
        @GetMapping
        public List<PartidoResponseDTO> listar(
                        @Parameter(description = "ID de la fecha", example = "1") @RequestParam(required = false) Long fechaId) {

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

                                .equipoLocal(partido.getEquipoLocal().getNombre())
                                .abreviaturaLocal(partido.getEquipoLocal().getAbreviatura())
                                .escudoLocal(partido.getEquipoLocal().getEscudo())

                                .equipoVisitante(partido.getEquipoVisitante().getNombre())
                                .abreviaturaVisitante(partido.getEquipoVisitante().getAbreviatura())
                                .escudoVisitante(partido.getEquipoVisitante().getEscudo())

                                .estado(partido.getEstado())
                                .matchday(partido.getMatchday())
                                .grupo(partido.getGrupo())
                                .stage(partido.getStage())

                                .resultadoLocal(partido.getResultadoLocal())
                                .resultadoVisitante(partido.getResultadoVisitante())

                                .fechaHora(partido.getFechaHora())
                                .fecha(
                                                partido.getFecha() != null
                                                                ? partido.getFecha().getNombre()
                                                                : "Sin fecha")

                                .build();
        }
}
