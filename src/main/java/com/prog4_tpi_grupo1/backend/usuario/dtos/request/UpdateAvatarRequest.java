package com.prog4_tpi_grupo1.backend.usuario.dtos.request;

import com.prog4_tpi_grupo1.backend.usuario.models.Avatar;

import jakarta.validation.constraints.NotNull;

public record UpdateAvatarRequest(
        @NotNull(message = "El avatar es obligatorio")
        Avatar avatar
) {}
