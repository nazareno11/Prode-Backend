package com.prog4_tpi_grupo1.backend.fecha.controller;

import com.prog4_tpi_grupo1.backend.fecha.dto.FechaResponseDTO;
import com.prog4_tpi_grupo1.backend.fecha.entity.EstadoFecha;
import com.prog4_tpi_grupo1.backend.fecha.entity.Fecha;
import com.prog4_tpi_grupo1.backend.fecha.repository.FechaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fechas")
@RequiredArgsConstructor
public class FechaController {

    private final FechaRepository fechaRepository;

    @GetMapping
    public List<FechaResponseDTO> listar(
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