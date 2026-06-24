package com.prog4_tpi_grupo1.backend.usuario.dtos.request;

import com.prog4_tpi_grupo1.backend.usuario.models.Avatar;

public record UpdateAvatarRequest(
        Avatar avatar
) {}