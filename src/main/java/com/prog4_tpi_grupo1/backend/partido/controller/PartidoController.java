package com.prog4_tpi_grupo1.backend.partido.controller;

import com.prog4_tpi_grupo1.backend.partido.dto.ResultadoPartidoDTO;
import com.prog4_tpi_grupo1.backend.partido.entity.Partido;
import com.prog4_tpi_grupo1.backend.partido.service.PartidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partidos")
@RequiredArgsConstructor
public class PartidoController {

    private final PartidoService partidoService;

    @PatchMapping("/{id}/iniciar")
    public Partido iniciarPartido(@PathVariable Long id) {

        return partidoService.iniciarPartido(id);
    }

    @PatchMapping("/{id}/resultado")
    public Partido cargarResultado(
            @PathVariable Long id,
            @RequestBody ResultadoPartidoDTO dto) {

        return partidoService.cargarResultado(id, dto);
    }
}
