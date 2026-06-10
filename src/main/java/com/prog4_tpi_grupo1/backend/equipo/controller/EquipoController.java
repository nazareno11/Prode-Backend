package com.prog4_tpi_grupo1.backend.equipo.controller;

import com.prog4_tpi_grupo1.backend.equipo.entity.Equipo;
import com.prog4_tpi_grupo1.backend.equipo.repository.EquipoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
public class EquipoController {

    private final EquipoRepository equipoRepository;

    @GetMapping
    public List<Equipo> listar() {
        return equipoRepository.findAll();
    }
}
