package com.prog4_tpi_grupo1.backend.pronostico.service.impl;

import com.prog4_tpi_grupo1.backend.auth.models.Usuario;
import com.prog4_tpi_grupo1.backend.partido.entity.Partido;
import com.prog4_tpi_grupo1.backend.partido.entity.EstadoPartido;
import com.prog4_tpi_grupo1.backend.partido.repository.PartidoRepository; 
import com.prog4_tpi_grupo1.backend.pronostico.dto.request.PronosticoRequestDTO;
import com.prog4_tpi_grupo1.backend.pronostico.dto.response.PronosticoResponseDTO;
import com.prog4_tpi_grupo1.backend.pronostico.entity.Pronostico;
import com.prog4_tpi_grupo1.backend.pronostico.repository.PronosticoRepository;
import com.prog4_tpi_grupo1.backend.pronostico.service.interfaces.IPronosticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PronosticoServiceImpl implements IPronosticoService {

    @Autowired 
    private PronosticoRepository pronosticoRepository;
    
    @Autowired 
    private PartidoRepository partidoRepository;

    // RF5.1: Crear o Modificar
    @Override
    @Transactional
    public PronosticoResponseDTO guardarOModificarPronostico(Usuario usuario, PronosticoRequestDTO request) {
        Partido partido = partidoRepository.findById(request.getPartidoId())
                .orElseThrow(() -> new RuntimeException("Partido no encontrado"));

        // Validación: Solo partidos en estado POR_JUGARSE
        if (partido.getEstado() != EstadoPartido.POR_JUGARSE) {
            throw new IllegalStateException("Solo se pueden registrar o modificar pronósticos para partidos en estado 'Por jugarse'.");
        }

        // Validación: Bloqueo faltándose 30 minutos o menos para el inicio
        if (LocalDateTime.now().isAfter(partido.getFechaHora().minusMinutes(30))) {
            throw new IllegalStateException("Acción denegada: Faltan menos de 30 minutos para el inicio del partido.");
        }

        // Restricción única / Modificación implícita (Upsert)
        Pronostico pronostico = pronosticoRepository.findByUsuarioIdAndPartidoId(usuario.getId(), partido.getId())
                .orElse(new Pronostico());

        pronostico.setUsuario(usuario);
        pronostico.setPartido(partido);
        pronostico.setGolesLocal(request.getGolesLocal());
        pronostico.setGolesVisitante(request.getGolesVisitante());

        Pronostico guardado = pronosticoRepository.save(pronostico);
        return mapToResponse(guardado);
    }

    // RF5.2: Consulta de Pronósticos Propios
    @Override
    @Transactional(readOnly = true)
    public List<PronosticoResponseDTO> obtenerPronosticosPropios(Usuario usuario, EstadoPartido estado) {
        // Si el estado viene nulo, la consulta en el Repositorio traerá todos sus pronósticos
        return pronosticoRepository.findByUsuarioIdAndPartidoEstado(usuario.getId(), estado)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // RF5.3: Consulta de Terceros
    @Override
    @Transactional(readOnly = true)
    public List<PronosticoResponseDTO> obtenerPronosticosTerceros(Long partidoId, Usuario usuarioActual) {
        Partido partido = partidoRepository.findById(partidoId)
                .orElseThrow(() -> new RuntimeException("Partido no encontrado"));

        // Validación: Solo se permite ver si el periodo de margen de bloqueo EXPIRÓ (Faltan menos de 30 min o ya se juega)
        boolean margenExpirado = LocalDateTime.now().isAfter(partido.getFechaHora().minusMinutes(30));
        if (!margenExpirado) {
            throw new IllegalStateException("No puedes consultar predicciones ajenas hasta que falten menos de 30 minutos para el inicio.");
        }

        // Retorna todos los pronósticos del partido excepto el del usuario logueado
        return pronosticoRepository.findByPartidoIdAndUsuarioIdNot(partidoId, usuarioActual.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Método auxiliar de mapeo (Si usan MapStruct en la carpeta 'mapper', cambien esto por su Mapper)
    private PronosticoResponseDTO mapToResponse(Pronostico p) {
        PronosticoResponseDTO dto = new PronosticoResponseDTO();
        dto.setId(p.getId());
        dto.setUsuarioId(p.getUsuario().getId());
        dto.setNombreUsuario(p.getUsuario().getUsername()); // o p.getUsuario().getNombre() según tengan en su modelo
        dto.setGolesLocal(p.getGolesLocal());
        dto.setGolesVisitante(p.getGolesVisitante());
        dto.setFechaRegistro(p.getFechaRegistro());
        
        if (p.getPartido() != null) {
            dto.setPartidoId(p.getPartido().getId());
            // Generamos un detalle lindo para el cliente: "Argentina vs Francia"
            String local = p.getPartido().getEquipoLocal() != null ? p.getPartido().getEquipoLocal().getNombre() : "Local";
            String visitante = p.getPartido().getEquipoVisitante() != null ? p.getPartido().getEquipoVisitante().getNombre() : "Visitante";
            dto.setDetallePartido(local + " vs " + visitante);
        }
        return dto;
    }
}