package com.prog4_tpi_grupo1.backend.partido.controller;

import com.prog4_tpi_grupo1.backend.partido.dto.ResultadoPartidoDTO;
import com.prog4_tpi_grupo1.backend.partido.entity.Partido;
import com.prog4_tpi_grupo1.backend.partido.repository.PartidoRepository;
import com.prog4_tpi_grupo1.backend.partido.service.PartidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partidos")
@RequiredArgsConstructor
public class PartidoController {

    private final PartidoService partidoService;
    private final PartidoRepository partidoRepository;

    @GetMapping
    public List<Partido> listar() {

        return partidoRepository.findAll();
    }

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
