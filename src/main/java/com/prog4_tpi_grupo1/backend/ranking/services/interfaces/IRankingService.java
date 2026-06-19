package com.prog4_tpi_grupo1.backend.ranking.services.interfaces;

import java.util.List;

import com.prog4_tpi_grupo1.backend.ranking.dtos.response.RankingUsuarioResponseDTO;

public interface IRankingService {

    List<RankingUsuarioResponseDTO> obtenerRankingGlobal();

}
