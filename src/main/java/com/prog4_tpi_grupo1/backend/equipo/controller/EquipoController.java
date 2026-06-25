package com.prog4_tpi_grupo1.backend.equipo.controller;

import com.prog4_tpi_grupo1.backend.equipo.entity.Equipo;
import com.prog4_tpi_grupo1.backend.equipo.repository.EquipoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Equipos",
        description = "Consulta de equipos participantes"
)
@RestController
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
public class EquipoController {

    private final EquipoRepository equipoRepository;

    @Operation(
            summary = "Listar equipos",
            description = "Obtiene todos los equipos registrados"
    )
    @GetMapping
    public List<Equipo> listar() {
        return equipoRepository.findAll();
    }
}
