package com.prog4_tpi_grupo1.backend.footballdata.controller;

import com.prog4_tpi_grupo1.backend.footballdata.service.FootballDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Administración Football Data",
        description = "Sincronización con Football Data API"
)
@RestController
@RequestMapping("/api/admin/football")
@RequiredArgsConstructor
public class FootballDataController {

    private final FootballDataService service;

    @Operation(
            summary = "Sincronizar equipos"
    )
    @PostMapping("/sync-teams")
    public String syncTeams() {

        service.syncTeams();

        return "Equipos sincronizados";
    }

    @Operation(
            summary = "Sincronizar partidos"
    )
    @PostMapping("/sync-matches")
    public String syncMatches() {

        service.syncMatches();

        return "Partidos sincronizados";
    }

    @PostMapping("/sync-fechas")
    public String syncFechas() {

        service.syncFechas();

        return "Fechas sincronizadas";
    }
}