package com.prog4_tpi_grupo1.backend.usuario.services.interfaces;

import com.prog4_tpi_grupo1.backend.usuario.dtos.response.UserProfileResponse;
import com.prog4_tpi_grupo1.backend.usuario.models.Avatar;

public interface IUsuarioService {

    UserProfileResponse getProfile();

    void updateAvatar(Avatar avatar);

}
