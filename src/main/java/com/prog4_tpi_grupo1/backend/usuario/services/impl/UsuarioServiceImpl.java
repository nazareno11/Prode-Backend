package com.prog4_tpi_grupo1.backend.usuario.services.impl;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.prog4_tpi_grupo1.backend.auth.models.Usuario;
import com.prog4_tpi_grupo1.backend.auth.repositories.IUsuarioRepository;
import com.prog4_tpi_grupo1.backend.pronostico.repository.PronosticoRepository;
import com.prog4_tpi_grupo1.backend.usuario.dtos.response.UserProfileResponse;
import com.prog4_tpi_grupo1.backend.usuario.mapper.UsuarioMapper;
import com.prog4_tpi_grupo1.backend.usuario.services.interfaces.IUsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {


        private final IUsuarioRepository usuarioRepository;
    private final PronosticoRepository pronosticoRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public UserProfileResponse getProfile() {

        Usuario usuario =
                (Usuario) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        int cantidadPronosticos =
                (int) pronosticoRepository.countByUsuario(usuario);

        int cantidadGrupos =
                usuario.getGrupos().size();

        return usuarioMapper.toProfileResponse(
                usuario,
                cantidadPronosticos,
                cantidadGrupos
        );
    }
}
