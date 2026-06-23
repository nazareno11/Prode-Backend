package com.prog4_tpi_grupo1.backend.usuario.mapper;

import org.springframework.stereotype.Component;

import com.prog4_tpi_grupo1.backend.auth.models.Usuario;
import com.prog4_tpi_grupo1.backend.usuario.dtos.response.UserProfileResponse;

@Component
public class UsuarioMapper {

    public UserProfileResponse toProfileResponse(
            Usuario usuario,
            Integer cantidadPronosticos,
            Integer cantidadGrupos
    ) {

        return new UserProfileResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getPuntosTotales(),
                usuario.getPlenosAcertados(),
                cantidadPronosticos,
                cantidadGrupos
        );
    }
}
