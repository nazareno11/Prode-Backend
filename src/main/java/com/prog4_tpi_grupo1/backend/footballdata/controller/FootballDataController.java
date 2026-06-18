package com.prog4_tpi_grupo1.backend.footballdata.controller;

import com.prog4_tpi_grupo1.backend.footballdata.service.FootballDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/football")
@RequiredArgsConstructor
public class FootballDataController {

    private final FootballDataService service;

    @PostMapping("/sync-teams")
    public String syncTeams() {

        service.syncTeams();

        return "Equipos sincronizados";
    }

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