package com.prog4_tpi_grupo1.backend.footballdata.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeamsResponseDTO {

    private CompetitionDTO competition;

    private List<TeamDTO> teams;
}
