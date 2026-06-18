package com.prog4_tpi_grupo1.backend.grupo.services.interfaces;

import java.util.List;

import com.prog4_tpi_grupo1.backend.auth.models.Usuario;
import com.prog4_tpi_grupo1.backend.grupo.dtos.request.CrearGrupoRequestDTO;
import com.prog4_tpi_grupo1.backend.grupo.dtos.request.UnirseGrupoRequestDTO;
import com.prog4_tpi_grupo1.backend.grupo.dtos.response.GrupoResponseDTO;
import com.prog4_tpi_grupo1.backend.grupo.dtos.response.RankingGrupoResponseDTO;

public interface IGrupoService {

    //Crear grupo
    GrupoResponseDTO crearGrupo(Usuario usuario, CrearGrupoRequestDTO request);

    //Unirse a un grupo
    GrupoResponseDTO unirseAGrupo(Usuario usuario, UnirseGrupoRequestDTO request);

    //Obtener los grupos del usuario
    List<GrupoResponseDTO> obtenerMisGrupos(Usuario usuario);

    //Ranking del grupo
    List<RankingGrupoResponseDTO> obtenerRanking(Long grupoId);

}
