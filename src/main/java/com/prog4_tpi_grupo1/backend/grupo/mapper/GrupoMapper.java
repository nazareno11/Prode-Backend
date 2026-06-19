package com.prog4_tpi_grupo1.backend.grupo.mapper;

import org.springframework.stereotype.Component;

import com.prog4_tpi_grupo1.backend.auth.models.Usuario;
import com.prog4_tpi_grupo1.backend.grupo.dtos.response.GrupoResponseDTO;
import com.prog4_tpi_grupo1.backend.grupo.dtos.response.RankingGrupoResponseDTO;
import com.prog4_tpi_grupo1.backend.grupo.models.Grupo;

@Component
public class GrupoMapper {

    public GrupoResponseDTO toGrupoResponse(Grupo grupo) {

        GrupoResponseDTO dto = new GrupoResponseDTO();

        dto.setId(grupo.getId());
        dto.setNombre(grupo.getNombre());
        dto.setCodigoInvitacion(grupo.getCodigoInvitacion());
        dto.setCreador(grupo.getCreador().getUsername());
        dto.setCantidadMiembros(grupo.getMiembros().size());

        return dto;
    }

    public RankingGrupoResponseDTO toRankingResponse(Usuario usuario, Integer posicion) {

        RankingGrupoResponseDTO dto = new RankingGrupoResponseDTO();

        dto.setUsuarioId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setPuntosTotales(usuario.getPuntosTotales());
        dto.setPlenosAcertados(usuario.getPlenosAcertados());
        dto.setPosicion(posicion);

        return dto;
    }
}
