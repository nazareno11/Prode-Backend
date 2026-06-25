package com.prog4_tpi_grupo1.backend.usuario.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prog4_tpi_grupo1.backend.usuario.dtos.request.UpdateAvatarRequest;
import com.prog4_tpi_grupo1.backend.usuario.dtos.response.UserProfileResponse;
import com.prog4_tpi_grupo1.backend.usuario.services.interfaces.IUsuarioService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsuarioController {

    private final IUsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile() {

        return ResponseEntity.ok(
                usuarioService.getProfile()
        );

    }
    @PatchMapping("/me/avatar")
    public ResponseEntity<Void> updateAvatar(
            @RequestBody UpdateAvatarRequest request
    ) {
        usuarioService.updateAvatar(request.avatar());
        return ResponseEntity.ok().build();
    }
  
}