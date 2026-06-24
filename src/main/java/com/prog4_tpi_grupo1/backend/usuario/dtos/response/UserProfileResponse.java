package com.prog4_tpi_grupo1.backend.usuario.dtos.response;

public record UserProfileResponse(

        Long id,
        String username,
        String email,
        Integer puntosTotales,
        Integer plenosAcertados,
        Integer cantidadPronosticos,
        Integer cantidadGrupos,
        Integer ranking,
        String avatar
) {}
