package com.prog4_tpi_grupo1.backend.footballdata.scheduler;

import com.prog4_tpi_grupo1.backend.footballdata.service.FootballDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FootballDataScheduler {

    private final FootballDataService footballDataService;

    @Scheduled(cron = "0 */5 * * * *")
    public void syncWorldCupData() {

        try {

            log.info("Iniciando sincronizacion automatica de partidos...");
            footballDataService.syncMatches();
            log.info("Sincronizacion finalizada correctamente.");

        } catch (Exception e) {

            log.error("Error durante la sincronizacion automatica", e);
        }
    }
}