package com.prog4_tpi_grupo1.backend.pronostico.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.prog4_tpi_grupo1.backend.auth.models.Usuario;
import com.prog4_tpi_grupo1.backend.auth.repositories.IUsuarioRepository;
import com.prog4_tpi_grupo1.backend.partido.entity.Partido;
import com.prog4_tpi_grupo1.backend.partido.entity.Tendencia;
import com.prog4_tpi_grupo1.backend.partido.repository.PartidoRepository;
import com.prog4_tpi_grupo1.backend.pronostico.entity.Pronostico;
import com.prog4_tpi_grupo1.backend.pronostico.repository.PronosticoRepository;
import com.prog4_tpi_grupo1.backend.pronostico.service.interfaces.IPuntuacionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PuntuacionServiceImpl implements IPuntuacionService {

    private final PronosticoRepository pronosticoRepository;
    private final PartidoRepository partidoRepository;
    private final IUsuarioRepository usuarioRepository;

@Override
public void calcularPuntosPartido(Long partidoId) {

    Partido partido = partidoRepository.findById(partidoId)
            .orElseThrow(() -> new RuntimeException("Partido no encontrado"));

    if (partido.getResultadoLocal() == null
            || partido.getResultadoVisitante() == null) {
        return;
    }

    List<Pronostico> pronosticos =
            pronosticoRepository.findByPartidoId(partidoId);

    Tendencia tendenciaReal = obtenerTendencia(
            partido.getResultadoLocal(),
            partido.getResultadoVisitante());

    for (Pronostico pronostico : pronosticos) {

        Usuario usuario = pronostico.getUsuario();

        Tendencia tendenciaPronosticada = obtenerTendencia(
                pronostico.getGolesLocal(),
                pronostico.getGolesVisitante());

        if (tendenciaPronosticada == tendenciaReal) {

            usuario.setPuntosTotales(usuario.getPuntosTotales() + 1);

            boolean pleno =
                    pronostico.getGolesLocal().equals(partido.getResultadoLocal())
                    && pronostico.getGolesVisitante().equals(partido.getResultadoVisitante());

            if (pleno) {
                usuario.setPuntosTotales(usuario.getPuntosTotales() + 2);
                usuario.setPlenosAcertados(usuario.getPlenosAcertados() + 1);
            }
        }

        usuarioRepository.save(usuario);
    }
}

    private Tendencia obtenerTendencia(Integer local, Integer visitante) {

        if (local > visitante) {
            return Tendencia.LOCAL;
        }

        if (local < visitante) {
            return Tendencia.VISITANTE;
        }

        return Tendencia.EMPATE;
    }

}
