package com.prog4_tpi_grupo1.backend.shared.config;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prog4_tpi_grupo1.backend.shared.config.esceptions.CustomException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones personalizadas.
     */
    @ExceptionHandler(CustomException.class)
    public ProblemDetail handleCustomException(CustomException ex) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());

        problem.setTitle(ex.getStatus().getReasonPhrase());

        if (ex.getErrors() != null && !ex.getErrors().isEmpty()) {
            problem.setProperty("errors", ex.getErrors());
        }

        return problem;

    }

    /**
     * Maneja las excepciones de validación.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .toList();

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problem.setTitle("Error de validación");

        problem.setDetail("Revise los campos indicados.");

        problem.setProperty("errors", errors);

        return problem;
    }

    /**
     * Maneja las excepciones genéricas.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error inesperado");
        problem.setTitle("Error interno del servidor");
        problem.setProperty("errors", List.of("Contacte al administrador"));
        return problem;
    }

    /**
     * Maneja las excepciones cuando falle el login
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);

        problem.setTitle("Credenciales inválidas");

        problem.setDetail("Email o contraseña incorrectos.");

        return problem;
    }
}
