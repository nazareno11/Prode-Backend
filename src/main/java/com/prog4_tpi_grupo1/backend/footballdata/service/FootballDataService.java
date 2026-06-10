package com.prog4_tpi_grupo1.backend.footballdata.service;

import com.prog4_tpi_grupo1.backend.equipo.entity.Equipo;
import com.prog4_tpi_grupo1.backend.equipo.repository.EquipoRepository;
import com.prog4_tpi_grupo1.backend.footballdata.client.FootballDataClient;
import com.prog4_tpi_grupo1.backend.footballdata.dto.TeamDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FootballDataService {

    private final FootballDataClient footballDataClient;
    private final EquipoRepository equipoRepository;

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
}
