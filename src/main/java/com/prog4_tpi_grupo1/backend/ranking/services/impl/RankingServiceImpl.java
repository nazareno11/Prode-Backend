package com.prog4_tpi_grupo1.backend.ranking.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prog4_tpi_grupo1.backend.auth.models.Usuario;
import com.prog4_tpi_grupo1.backend.auth.repositories.IUsuarioRepository;
import com.prog4_tpi_grupo1.backend.ranking.dtos.response.RankingUsuarioResponseDTO;
import com.prog4_tpi_grupo1.backend.ranking.mapper.RankingMapper;
import com.prog4_tpi_grupo1.backend.ranking.services.interfaces.IRankingService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements IRankingService {

    private final IUsuarioRepository usuarioRepository;
    private final RankingMapper rankingMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RankingUsuarioResponseDTO> obtenerRankingGlobal() {

        List<Usuario> usuarios =
                usuarioRepository.findAllByOrderByPuntosTotalesDescPlenosAcertadosDescUsernameAsc();

        List<RankingUsuarioResponseDTO> ranking = new ArrayList<>();

        for (int i = 0; i < usuarios.size(); i++) {

            ranking.add(
                    rankingMapper.toRankingResponse(
                            usuarios.get(i),
                            i + 1
                    )
            );
        }

        return ranking;
    }
}
