package com.prog4_tpi_grupo1.backend.ranking.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prog4_tpi_grupo1.backend.ranking.dtos.response.RankingUsuarioResponseDTO;
import com.prog4_tpi_grupo1.backend.ranking.services.interfaces.IRankingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final IRankingService rankingService;

    @GetMapping
    public ResponseEntity<List<RankingUsuarioResponseDTO>> obtenerRankingGlobal() {

        return ResponseEntity.ok(
                rankingService.obtenerRankingGlobal()
        );
    }
}