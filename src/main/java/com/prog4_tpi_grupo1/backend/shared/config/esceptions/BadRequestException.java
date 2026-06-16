package com.prog4_tpi_grupo1.backend.shared.config.esceptions;

import java.util.List;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomException {

    public BadRequestException(String message) {
        super(message,
                HttpStatus.BAD_REQUEST,
                List.of(message));
    }

    public BadRequestException(String message,
                               List<String> errors) {

        super(message,
                HttpStatus.BAD_REQUEST,
                errors);
    }
}
