package com.prog4_tpi_grupo1.backend.footballdata.dto;

import lombok.Data;

@Data
public class MatchDTO {

    private Long id;

    private String utcDate;

    private String status;

    private Integer matchday;

    private String stage;

    private String group;

    private MatchTeamDTO homeTeam;

    private MatchTeamDTO awayTeam;

    private ScoreWrapperDTO score;
}
