package com.prog4_tpi_grupo1.backend.auth.dtos.response;

public record AuthResponse(
        String token,
        String type,
        String username
) {

    public AuthResponse(String token) {
        this(token, "Bearer", null);
    }

}
