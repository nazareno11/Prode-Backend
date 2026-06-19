package com.prog4_tpi_grupo1.backend.pronostico.service.interfaces;

import com.prog4_tpi_grupo1.backend.auth.models.Usuario;
import com.prog4_tpi_grupo1.backend.partido.entity.EstadoPartido;
import com.prog4_tpi_grupo1.backend.pronostico.dto.request.PronosticoRequestDTO;
import com.prog4_tpi_grupo1.backend.pronostico.dto.response.PronosticoResponseDTO;

import java.util.List;

public interface IPronosticoService {
    
    // RF5.1: Crear o Modificar Pronóstico
    PronosticoResponseDTO guardarOModificarPronostico(Usuario usuario, PronosticoRequestDTO request);
    
    // RF5.2: Consulta de Pronósticos Propios (Filtrado opcional por Estado de Partido)
    List<PronosticoResponseDTO> obtenerPronosticosPropios(Usuario usuario, EstadoPartido estado);
    
    // RF5.3: Consulta de Pronósticos de Terceros
    List<PronosticoResponseDTO> obtenerPronosticosTerceros(Long partidoId, Usuario usuarioActual);
}