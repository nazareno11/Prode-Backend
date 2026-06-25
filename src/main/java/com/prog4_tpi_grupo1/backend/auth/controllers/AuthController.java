package com.prog4_tpi_grupo1.backend.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prog4_tpi_grupo1.backend.auth.dtos.request.LoginRequest;
import com.prog4_tpi_grupo1.backend.auth.dtos.request.RegisterRequest;
import com.prog4_tpi_grupo1.backend.auth.dtos.response.AuthResponse;
import com.prog4_tpi_grupo1.backend.auth.services.interfaces.IAuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(
        name = "Autenticación",
        description = "Registro e inicio de sesión"
)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthenticationService authService;

    @Operation(
            summary = "Registrar usuario",
            description = "Crea una nueva cuenta y devuelve un JWT"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario y devuelve un JWT"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login correcto"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authService.authenticate(request)
        );
    }
}
