package com.prog4_tpi_grupo1.backend.grupo.services.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prog4_tpi_grupo1.backend.auth.models.Usuario;
import com.prog4_tpi_grupo1.backend.auth.repositories.IUsuarioRepository;
import com.prog4_tpi_grupo1.backend.grupo.dtos.request.CrearGrupoRequestDTO;
import com.prog4_tpi_grupo1.backend.grupo.dtos.request.UnirseGrupoRequestDTO;
import com.prog4_tpi_grupo1.backend.grupo.dtos.response.GrupoResponseDTO;
import com.prog4_tpi_grupo1.backend.grupo.dtos.response.RankingGrupoResponseDTO;
import com.prog4_tpi_grupo1.backend.grupo.mapper.GrupoMapper;
import com.prog4_tpi_grupo1.backend.grupo.models.Grupo;
import com.prog4_tpi_grupo1.backend.grupo.repositories.IGrupoRepository;
import com.prog4_tpi_grupo1.backend.grupo.services.interfaces.IGrupoService;
import com.prog4_tpi_grupo1.backend.shared.config.esceptions.NotFoundException;
import com.prog4_tpi_grupo1.backend.shared.config.esceptions.ConflictException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GrupoServiceImpl implements IGrupoService {

        private final IGrupoRepository grupoRepository;
        private final GrupoMapper grupoMapper;
        private final IUsuarioRepository usuarioRepository;

        private String generarCodigoInvitacion() {

                String codigo;

                do {
                        codigo = UUID.randomUUID()
                                        .toString()
                                        .replace("-", "")
                                        .substring(0, 8)
                                        .toUpperCase();

                } while (grupoRepository.existsByCodigoInvitacion(codigo));

                return codigo;
        }

        @Override
        @Transactional
        public GrupoResponseDTO crearGrupo(
                        Usuario usuario,
                        CrearGrupoRequestDTO request) {

                Grupo grupo = Grupo.builder()
                                .nombre(request.getNombre())
                                .codigoInvitacion(generarCodigoInvitacion())
                                .creador(usuario)
                                .build();

                grupo.getMiembros().add(usuario);

                Grupo guardado = grupoRepository.save(grupo);

                return grupoMapper.toGrupoResponse(guardado);
        }

        @Override
        @Transactional
        public GrupoResponseDTO unirseAGrupo(
                        Usuario usuario,
                        UnirseGrupoRequestDTO request) {

                Grupo grupo = grupoRepository
                                .findByCodigoInvitacion(request.getCodigoInvitacion())
                                .orElseThrow(() -> new NotFoundException("Grupo no encontrado"));

                boolean yaPertenece = grupo.getMiembros()
                                .stream()
                                .anyMatch(u -> u.getId().equals(usuario.getId()));

                if (yaPertenece) {
                        throw new ConflictException("Ya perteneces a este grupo");
                }

                grupo.getMiembros().add(usuario);

                Grupo actualizado = grupoRepository.save(grupo);

                return grupoMapper.toGrupoResponse(actualizado);
        }

        @Override
        @Transactional(readOnly = true)
        public List<GrupoResponseDTO> obtenerMisGrupos(Usuario usuario) {

                Usuario usuarioBD = usuarioRepository
                                .findByIdConGrupos(usuario.getId())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                return usuarioBD.getGrupos()
                                .stream()
                                .map(grupoMapper::toGrupoResponse)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<RankingGrupoResponseDTO> obtenerRanking(Long grupoId) {

                Grupo grupo = grupoRepository.findById(grupoId)
                                .orElseThrow(() -> new NotFoundException("Grupo no encontrado"));

                List<Usuario> miembros = grupo.getMiembros()
                                .stream()
                                .sorted(
                                                Comparator
                                                                .comparing(Usuario::getPuntosTotales,
                                                                                Comparator.reverseOrder())
                                                                .thenComparing(Usuario::getPlenosAcertados,
                                                                                Comparator.reverseOrder())
                                                                .thenComparing(Usuario::getUsername))
                                .toList();

                List<RankingGrupoResponseDTO> ranking = new ArrayList<>();

                for (int i = 0; i < miembros.size(); i++) {
                        ranking.add(
                                        grupoMapper.toRankingResponse(
                                                        miembros.get(i),
                                                        i + 1));
                }

                return ranking;
        }

}