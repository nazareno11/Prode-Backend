package com.prog4_tpi_grupo1.backend.pronostico.controller;

import com.prog4_tpi_grupo1.backend.auth.models.Usuario;
import com.prog4_tpi_grupo1.backend.partido.entity.EstadoPartido;
import com.prog4_tpi_grupo1.backend.pronostico.dto.request.PronosticoRequestDTO;
import com.prog4_tpi_grupo1.backend.pronostico.dto.response.PronosticoResponseDTO;
import com.prog4_tpi_grupo1.backend.pronostico.service.interfaces.IPronosticoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pronosticos")
public class PronosticoController {

    @Autowired
    private IPronosticoService pronosticoService;

    // RF5.1: Crear o Modificar Pronóstico
    @PostMapping
    public ResponseEntity<PronosticoResponseDTO> guardarPronostico(
            @Valid @RequestBody PronosticoRequestDTO request,
            @AuthenticationPrincipal Usuario usuario) {
        
        PronosticoResponseDTO response = pronosticoService.guardarOModificarPronostico(usuario, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // RF5.2: Consulta de Pronósticos Propios
    // Ejemplo de uso en la URL: /api/pronosticos/mis-pronosticos?estado=POR_JUGARSE
    @GetMapping("/mis-pronosticos")
    public ResponseEntity<List<PronosticoResponseDTO>> listarMisPronosticos(
            @RequestParam(value = "estado", required = false) EstadoPartido estado,
            @AuthenticationPrincipal Usuario usuario) {
            
        List<PronosticoResponseDTO> response = pronosticoService.obtenerPronosticosPropios(usuario, estado);
        return ResponseEntity.ok(response);
    }

    // RF5.3: Consulta de Pronósticos de Terceros
    @GetMapping("/partido/{partidoId}/terceros")
    public ResponseEntity<List<PronosticoResponseDTO>> listarPronosticosTerceros(
            @PathVariable Long partidoId,
            @AuthenticationPrincipal Usuario usuario) {
            
        List<PronosticoResponseDTO> response = pronosticoService.obtenerPronosticosTerceros(partidoId, usuario);
        return ResponseEntity.ok(response);
    }
}