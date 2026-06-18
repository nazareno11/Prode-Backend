package com.prog4_tpi_grupo1.backend.ranking.mapper;

import com.prog4_tpi_grupo1.backend.auth.models.Usuario;
import com.prog4_tpi_grupo1.backend.ranking.dtos.response.RankingUsuarioResponseDTO;

public class RankingMapper {
    
    public RankingUsuarioResponseDTO toRankingResponse(
        Usuario usuario,
        Integer posicion) {

    RankingUsuarioResponseDTO dto = new RankingUsuarioResponseDTO();

    dto.setUsuarioId(usuario.getId());
    dto.setUsername(usuario.getUsername());
    dto.setPuntosTotales(usuario.getPuntosTotales());
    dto.setPlenosAcertados(usuario.getPlenosAcertados());
    dto.setPosicion(posicion);

    return dto;
}

}
