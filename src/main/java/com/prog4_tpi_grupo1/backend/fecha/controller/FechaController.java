package com.prog4_tpi_grupo1.backend.fecha.controller;

import com.prog4_tpi_grupo1.backend.fecha.dto.FechaResponseDTO;
import com.prog4_tpi_grupo1.backend.fecha.entity.EstadoFecha;
import com.prog4_tpi_grupo1.backend.fecha.entity.Fecha;
import com.prog4_tpi_grupo1.backend.fecha.repository.FechaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Fechas",
        description = "Gestión y consulta de jornadas"
)
@RestController
@RequestMapping("/api/fechas")
@RequiredArgsConstructor
public class FechaController {

    private final FechaRepository fechaRepository;

    @Operation(
            summary = "Listar fechas",
            description = "Permite listar todas las fechas o filtrar por estado"
    )
    @GetMapping
    public List<FechaResponseDTO> listar(
            @Parameter(
                    description = "Estado de la fecha",
                    example = "FINALIZADA"
            )
            @RequestParam(required = false)
            EstadoFecha estado) {

        List<Fecha> fechas;

        if (estado != null) {
            fechas = fechaRepository.findByEstado(estado);
        } else {
            fechas = fechaRepository.findAll();
        }

        return fechas.stream().map(this::toDto).toList();
    }

    private FechaResponseDTO toDto(Fecha fecha) {

        return FechaResponseDTO.builder()
                .id(fecha.getId())
                .nombre(fecha.getNombre())
                .grupo(fecha.getGrupo())
                .matchday(fecha.getMatchday())
                .estado(fecha.getEstado())
                .build();
    }
}