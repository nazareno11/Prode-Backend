package com.prog4_tpi_grupo1.backend.usuario.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prog4_tpi_grupo1.backend.usuario.dtos.request.UpdateAvatarRequest;
import com.prog4_tpi_grupo1.backend.usuario.dtos.response.AvatarResponse;
import com.prog4_tpi_grupo1.backend.usuario.models.Avatar;
import com.prog4_tpi_grupo1.backend.usuario.services.interfaces.IUsuarioService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/avatars")
@RequiredArgsConstructor
public class AvatarController {
    private final IUsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<AvatarResponse>> getAvatars() {

        List<AvatarResponse> avatars =
                Arrays.stream(Avatar.values())
                        .map(a -> new AvatarResponse(
                                a.name(),
                                a.getUrl()
                        ))
                        .toList();

        return ResponseEntity.ok(avatars);
    }


}
