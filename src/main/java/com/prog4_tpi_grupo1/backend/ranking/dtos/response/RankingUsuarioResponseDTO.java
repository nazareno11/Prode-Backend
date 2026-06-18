package com.prog4_tpi_grupo1.backend.ranking.dtos.response;

import lombok.Data;

@Data
public class RankingUsuarioResponseDTO {

    private Long usuarioId;

    private String username;

    private Integer puntosTotales;

    private Integer plenosAcertados;

    private Integer posicion;

}
