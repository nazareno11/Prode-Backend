package com.prog4_tpi_grupo1.backend.grupo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prog4_tpi_grupo1.backend.auth.models.Usuario;
import com.prog4_tpi_grupo1.backend.grupo.dtos.request.CrearGrupoRequestDTO;
import com.prog4_tpi_grupo1.backend.grupo.dtos.request.UnirseGrupoRequestDTO;
import com.prog4_tpi_grupo1.backend.grupo.dtos.response.GrupoResponseDTO;
import com.prog4_tpi_grupo1.backend.grupo.dtos.response.RankingGrupoResponseDTO;
import com.prog4_tpi_grupo1.backend.grupo.services.interfaces.IGrupoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/grupos")
@RequiredArgsConstructor
public class GrupoController {

    private final IGrupoService grupoService;

    @PostMapping
    public ResponseEntity<GrupoResponseDTO> crearGrupo(
            @Valid @RequestBody CrearGrupoRequestDTO request,
            @AuthenticationPrincipal Usuario usuario) {

        GrupoResponseDTO response = grupoService.crearGrupo(usuario, request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/unirse")
    public ResponseEntity<GrupoResponseDTO> unirseAGrupo(
            @Valid @RequestBody UnirseGrupoRequestDTO request,
            @AuthenticationPrincipal Usuario usuario) {

        GrupoResponseDTO response = grupoService.unirseAGrupo(usuario, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/mis-grupos")
    public ResponseEntity<List<GrupoResponseDTO>> obtenerMisGrupos(
            @AuthenticationPrincipal Usuario usuario) {

        return ResponseEntity.ok(
                grupoService.obtenerMisGrupos(usuario)
        );
    }

    @GetMapping("/{grupoId}/ranking")
    public ResponseEntity<List<RankingGrupoResponseDTO>> obtenerRanking(
            @PathVariable Long grupoId) {

        return ResponseEntity.ok(
                grupoService.obtenerRanking(grupoId)
        );
    }

}