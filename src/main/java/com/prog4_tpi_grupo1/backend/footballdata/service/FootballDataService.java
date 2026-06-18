package com.prog4_tpi_grupo1.backend.footballdata.service;

import com.prog4_tpi_grupo1.backend.equipo.entity.Equipo;
import com.prog4_tpi_grupo1.backend.equipo.repository.EquipoRepository;
import com.prog4_tpi_grupo1.backend.fecha.entity.EstadoFecha;
import com.prog4_tpi_grupo1.backend.fecha.entity.Fecha;
import com.prog4_tpi_grupo1.backend.fecha.repository.FechaRepository;
import com.prog4_tpi_grupo1.backend.footballdata.client.FootballDataClient;
import com.prog4_tpi_grupo1.backend.footballdata.dto.MatchDTO;
import com.prog4_tpi_grupo1.backend.footballdata.dto.TeamDTO;
import com.prog4_tpi_grupo1.backend.partido.entity.EstadoPartido;
import com.prog4_tpi_grupo1.backend.partido.entity.Partido;
import com.prog4_tpi_grupo1.backend.partido.repository.PartidoRepository;
import com.prog4_tpi_grupo1.backend.pronostico.service.interfaces.IPuntuacionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FootballDataService {

    private final FootballDataClient footballDataClient;
    private final EquipoRepository equipoRepository;
    private final PartidoRepository partidoRepository;
    private final FechaRepository fechaRepository;

    // puntuacion
    private final IPuntuacionService puntuacionService;

    public void syncTeams() {

        var response = footballDataClient.getWorldCupTeams();

        for (TeamDTO team : response.getTeams()) {

            boolean existe = equipoRepository
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

        Set<Fecha> fechasActualizadas = new HashSet<>();

        for (MatchDTO match : response.getMatches()) {

            if (match.getHomeTeam() == null
                    || match.getAwayTeam() == null
                    || match.getHomeTeam().getId() == null
                    || match.getAwayTeam().getId() == null) {

                continue;
            }

            Fecha fecha = fechaRepository.findByGrupoAndMatchday(
                    match.getGroup(),
                    match.getMatchday()).orElseThrow();

            fechasActualizadas.add(fecha);

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

                // Guardamos el estado anterior
                EstadoPartido estadoAnterior = partido.getEstado();

                // Actualizamos el partido
                partido.setEstado(convertirEstado(match.getStatus()));
                partido.setResultadoLocal(golesLocal);
                partido.setResultadoVisitante(golesVisitante);

                partidoRepository.save(partido);

                // Si recien paso a finalizado, calculamos los puntos
                if (estadoAnterior != EstadoPartido.FINALIZADO
                        && partido.getEstado() == EstadoPartido.FINALIZADO) {

                    puntuacionService.calcularPuntosPartido(partido.getId());
                }

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
                        .fecha(fecha)
                        .build();

                partidoRepository.save(partido);
            }
        }
        for (Fecha fecha : fechasActualizadas) {
            actualizarEstadoFecha(fecha);
        }
    }

    private EstadoPartido convertirEstado(String estadoApi) {

        return switch (estadoApi) {

            case "SCHEDULED" -> EstadoPartido.POR_JUGARSE;
            case "IN_PLAY",
                    "PAUSED" ->
                EstadoPartido.EN_JUEGO;
            case "FINISHED" -> EstadoPartido.FINALIZADO;
            default -> EstadoPartido.POR_JUGARSE;
        };
    }

    public void syncFechas() {

        var response = footballDataClient.getWorldCupMatches();

        for (MatchDTO match : response.getMatches()) {

            String grupo = match.getGroup();

            Integer matchday = match.getMatchday();

            if (grupo == null || matchday == null) {
                continue;
            }

            boolean existe = fechaRepository
                    .findByGrupoAndMatchday(
                            grupo,
                            matchday)
                    .isPresent();

            if (existe) {
                continue;
            }

            Fecha fecha = Fecha.builder()
                    .nombre(
                            grupo + " - Fecha " + matchday)
                    .grupo(grupo)
                    .matchday(matchday)
                    .estado(EstadoFecha.PROGRAMADA)
                    .build();

            fechaRepository.save(fecha);
        }
    }

    @Transactional
    public void actualizarEstadoFecha(Fecha fecha) {

        List<Partido> partidos = partidoRepository.findByFecha(fecha);

        if (partidos.isEmpty()) {
            return;
        }

        boolean todosFinalizados = partidos
                .stream()
                .allMatch(p -> p.getEstado() == EstadoPartido.FINALIZADO);

        if (todosFinalizados) {

            fecha.setEstado(EstadoFecha.FINALIZADA);
            fechaRepository.save(fecha);
            return;
        }

        boolean algunoEnJuego = partidos.stream()
                .anyMatch(p -> p.getEstado() == EstadoPartido.EN_JUEGO);

        if (algunoEnJuego) {

            fecha.setEstado(EstadoFecha.EN_JUEGO);
            fechaRepository.save(fecha);
            return;
        }

        fecha.setEstado(EstadoFecha.PROGRAMADA);

        fechaRepository.save(fecha);
    }
}
