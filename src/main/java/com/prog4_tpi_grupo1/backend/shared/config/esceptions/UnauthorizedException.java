package com.prog4_tpi_grupo1.backend.shared.config.esceptions;

import java.util.List;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CustomException {

    public UnauthorizedException(String message) {
        super(message,
                HttpStatus.UNAUTHORIZED,
                List.of(message));
    }

}