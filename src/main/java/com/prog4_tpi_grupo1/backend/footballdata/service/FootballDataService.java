package com.prog4_tpi_grupo1.backend.footballdata.service;

import com.prog4_tpi_grupo1.backend.equipo.entity.Equipo;
import com.prog4_tpi_grupo1.backend.equipo.repository.EquipoRepository;
import com.prog4_tpi_grupo1.backend.footballdata.client.FootballDataClient;
import com.prog4_tpi_grupo1.backend.footballdata.dto.MatchDTO;
import com.prog4_tpi_grupo1.backend.footballdata.dto.TeamDTO;
import com.prog4_tpi_grupo1.backend.partido.entity.EstadoPartido;
import com.prog4_tpi_grupo1.backend.partido.entity.Partido;
import com.prog4_tpi_grupo1.backend.partido.repository.PartidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FootballDataService {

    private final FootballDataClient footballDataClient;
    private final EquipoRepository equipoRepository;
    private final PartidoRepository partidoRepository;

    public void syncTeams() {

        var response =
                footballDataClient.getWorldCupTeams();

        for (TeamDTO team : response.getTeams()) {

            boolean existe =
                    equipoRepository
                            .findByExternalId(team.getId())
                            .isPresent();

            if (!existe) {

                Equipo equipo = Equipo.builder()
                        .externalId(team.getId())
                        .nombre(team.getName())
                        .abreviatura(team.getTla())
                        .escudo(team.getCrest())
                        .build();

                equipoRepository.save(equipo);
            }
        }
    }

    public void syncMatches() {

        var response = footballDataClient.getWorldCupMatches();

        for (MatchDTO match : response.getMatches()) {

            if (match.getHomeTeam() == null
                    || match.getAwayTeam() == null
                    || match.getHomeTeam().getId() == null
                    || match.getAwayTeam().getId() == null) {

                continue;
            }

            Optional<Equipo> localOpt = equipoRepository.findByExternalId(match.getHomeTeam().getId());

            Optional<Equipo> visitanteOpt = equipoRepository.findByExternalId(match.getAwayTeam().getId());

            if (localOpt.isEmpty()
                    || visitanteOpt.isEmpty()) {

                continue;
            }

            Equipo local = localOpt.get();
            Equipo visitante = visitanteOpt.get();

            Integer golesLocal = null;
            Integer golesVisitante = null;

            if (match.getScore() != null && match.getScore().getFullTime() != null) {

                golesLocal = match.getScore().getFullTime().getHome();

                golesVisitante = match.getScore().getFullTime().getAway();
            }

            Optional<Partido> partidoExistente = partidoRepository.findByExternalId(match.getId());

            if (partidoExistente.isPresent()) {

                Partido partido = partidoExistente.get();
                partido.setEstado(convertirEstado(match.getStatus()));
                partido.setResultadoLocal(golesLocal);
                partido.setResultadoVisitante(golesVisitante);
                partidoRepository.save(partido);

            } else {

                Partido partido = Partido.builder()
                        .externalId(match.getId())
                        .fechaHora(OffsetDateTime.parse(match.getUtcDate()).toLocalDateTime())
                        .estado(convertirEstado(match.getStatus()))
                        .matchday(match.getMatchday())
                        .stage(match.getStage())
                        .grupo(match.getGroup())
                        .resultadoLocal(golesLocal)
                        .resultadoVisitante(golesVisitante)
                        .equipoLocal(local)
                        .equipoVisitante(visitante)
                        .build();

                partidoRepository.save(partido);
            }
        }
    }

    private EstadoPartido convertirEstado(String estadoApi) {

        return switch (estadoApi) {

            case "SCHEDULED" -> EstadoPartido.POR_JUGARSE;
            case "IN_PLAY",
                 "PAUSED" -> EstadoPartido.EN_JUEGO;
            case "FINISHED" -> EstadoPartido.FINALIZADO;
            default -> EstadoPartido.POR_JUGARSE;
        };
    }
}
