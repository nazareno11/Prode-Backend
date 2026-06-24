package com.prog4_tpi_grupo1.backend.usuario.services.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.prog4_tpi_grupo1.backend.auth.models.Usuario;
import com.prog4_tpi_grupo1.backend.auth.repositories.IUsuarioRepository;
import com.prog4_tpi_grupo1.backend.pronostico.repository.PronosticoRepository;
import com.prog4_tpi_grupo1.backend.ranking.services.interfaces.IRankingService;
import com.prog4_tpi_grupo1.backend.usuario.dtos.response.UserProfileResponse;
import com.prog4_tpi_grupo1.backend.usuario.mapper.UsuarioMapper;
import com.prog4_tpi_grupo1.backend.usuario.models.Avatar;
import com.prog4_tpi_grupo1.backend.usuario.services.interfaces.IUsuarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {

        private final IUsuarioRepository usuarioRepository;
        private final PronosticoRepository pronosticoRepository;
        private final UsuarioMapper usuarioMapper;
        private final IRankingService rankingService;

        @Override
        public UserProfileResponse getProfile() {

                Usuario usuarioAuth = (Usuario) SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal();

                Usuario usuario = usuarioRepository
                                .findByIdConGrupos(usuarioAuth.getId())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                int cantidadPronosticos = (int) pronosticoRepository.countByUsuario(usuario);

                int cantidadGrupos = usuario.getGrupos().size();
                
                Integer ranking = rankingService.obtenerPosicionUsuario(usuario.getId());

                return usuarioMapper.toProfileResponse(
                                usuario,
                                cantidadPronosticos,
                                cantidadGrupos,
                                ranking);
        }

        /*AVATAR */
        @Transactional
        public void updateAvatar(Avatar avatar) {

        Usuario usuario = getUsuarioLogueado();

        usuario.setAvatar(avatar);

        usuarioRepository.save(usuario);
        }

        private Usuario getUsuarioLogueado() {

        Usuario auth = (Usuario) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return usuarioRepository
                .findById(auth.getId())
                .orElseThrow();
        }
        
        
}
