package com.prog4_tpi_grupo1.backend.grupo.dtos.response;

import lombok.Data;

@Data
public class RankingGrupoResponseDTO {

    private Long usuarioId;

    private String username;

    private Integer puntosTotales;

    private Integer plenosAcertados;

    private Integer posicion;

}
