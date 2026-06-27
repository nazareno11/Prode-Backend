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
import com.prog4_tpi_grupo1.backend.shared.config.esceptions.NotFoundException;

import org.springframework.transaction.annotation.Transactional;
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
                .orElseThrow(() -> new NotFoundException("Partido no encontrado"));

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

            int puntosAnteriores = valorEntero(pronostico.getPuntosObtenidos());
            boolean plenoAnterior = Boolean.TRUE.equals(pronostico.getPlenoAcertado());

            int puntosNuevos = calcularPuntosPronostico(pronostico, partido, tendenciaReal);
            boolean plenoNuevo = esPleno(pronostico, partido);

            usuario.setPuntosTotales(valorEntero(usuario.getPuntosTotales()) + puntosNuevos - puntosAnteriores);

            if (plenoAnterior != plenoNuevo) {
                int diferenciaPlenos = plenoNuevo ? 1 : -1;
                usuario.setPlenosAcertados(valorEntero(usuario.getPlenosAcertados()) + diferenciaPlenos);
            }

            pronostico.setPuntosObtenidos(puntosNuevos);
            pronostico.setPlenoAcertado(plenoNuevo);

            pronosticoRepository.save(pronostico);
            usuarioRepository.save(usuario);
        }
    }

    private int calcularPuntosPronostico(Pronostico pronostico, Partido partido, Tendencia tendenciaReal) {

        Tendencia tendenciaPronosticada = obtenerTendencia(
                pronostico.getGolesLocal(),
                pronostico.getGolesVisitante());

        if (tendenciaPronosticada != tendenciaReal) {
            return 0;
        }

        return esPleno(pronostico, partido) ? 3 : 1;
    }

    private boolean esPleno(Pronostico pronostico, Partido partido) {

        return pronostico.getGolesLocal().equals(partido.getResultadoLocal())
                && pronostico.getGolesVisitante().equals(partido.getResultadoVisitante());
    }

    private int valorEntero(Integer valor) {

        return valor == null ? 0 : valor;
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
