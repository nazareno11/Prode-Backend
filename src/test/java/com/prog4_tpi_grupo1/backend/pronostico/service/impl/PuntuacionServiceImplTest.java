package com.prog4_tpi_grupo1.backend.pronostico.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.prog4_tpi_grupo1.backend.auth.models.Usuario;
import com.prog4_tpi_grupo1.backend.auth.repositories.IUsuarioRepository;
import com.prog4_tpi_grupo1.backend.partido.entity.Partido;
import com.prog4_tpi_grupo1.backend.partido.repository.PartidoRepository;
import com.prog4_tpi_grupo1.backend.pronostico.entity.Pronostico;
import com.prog4_tpi_grupo1.backend.pronostico.repository.PronosticoRepository;

class PuntuacionServiceImplTest {

    @Test
    void calcularPuntosPartidoNoDuplicaPuntosSiSeEjecutaDosVeces() {

        Partido partido = Partido.builder()
                .id(1L)
                .resultadoLocal(2)
                .resultadoVisitante(1)
                .build();

        Usuario usuario = Usuario.builder()
                .puntosTotales(0)
                .plenosAcertados(0)
                .build();

        Pronostico pronostico = new Pronostico();
        pronostico.setUsuario(usuario);
        pronostico.setPartido(partido);
        pronostico.setGolesLocal(2);
        pronostico.setGolesVisitante(1);

        PuntuacionServiceImpl service =
                new PuntuacionServiceImpl(
                        pronosticoRepository(pronostico),
                        partidoRepository(partido),
                        usuarioRepository());

        service.calcularPuntosPartido(1L);
        service.calcularPuntosPartido(1L);

        assertThat(usuario.getPuntosTotales()).isEqualTo(3);
        assertThat(usuario.getPlenosAcertados()).isEqualTo(1);
        assertThat(pronostico.getPuntosObtenidos()).isEqualTo(3);
        assertThat(pronostico.getPlenoAcertado()).isTrue();
    }

    @Test
    void calcularPuntosPartidoCorrigePuntosSiCambiaElResultadoFinal() {

        Partido partido = Partido.builder()
                .id(1L)
                .resultadoLocal(2)
                .resultadoVisitante(1)
                .build();

        Usuario usuario = Usuario.builder()
                .puntosTotales(0)
                .plenosAcertados(0)
                .build();

        Pronostico pronostico = new Pronostico();
        pronostico.setUsuario(usuario);
        pronostico.setPartido(partido);
        pronostico.setGolesLocal(2);
        pronostico.setGolesVisitante(1);

        PuntuacionServiceImpl service =
                new PuntuacionServiceImpl(
                        pronosticoRepository(pronostico),
                        partidoRepository(partido),
                        usuarioRepository());

        service.calcularPuntosPartido(1L);

        partido.setResultadoLocal(3);
        partido.setResultadoVisitante(1);
        service.calcularPuntosPartido(1L);

        assertThat(usuario.getPuntosTotales()).isEqualTo(1);
        assertThat(usuario.getPlenosAcertados()).isZero();
        assertThat(pronostico.getPuntosObtenidos()).isEqualTo(1);
        assertThat(pronostico.getPlenoAcertado()).isFalse();
    }

    private PartidoRepository partidoRepository(Partido partido) {

        return proxy(PartidoRepository.class, (unused, method, args) -> {
            if (method.getName().equals("findById")) {
                return Optional.of(partido);
            }

            return defaultValue(method.getReturnType(), args);
        });
    }

    private PronosticoRepository pronosticoRepository(Pronostico pronostico) {

        return proxy(PronosticoRepository.class, (unused, method, args) -> {
            if (method.getName().equals("findByPartidoId")) {
                return List.of(pronostico);
            }

            return defaultValue(method.getReturnType(), args);
        });
    }

    private IUsuarioRepository usuarioRepository() {

        return proxy(IUsuarioRepository.class, (unused, method, args) -> defaultValue(method.getReturnType(), args));
    }

    @SuppressWarnings("unchecked")
    private <T> T proxy(Class<T> type, InvocationHandler invocationHandler) {

        return (T) Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class<?>[] { type },
                invocationHandler);
    }

    private Object defaultValue(Class<?> returnType, Object[] args) {

        if (returnType == Void.TYPE) {
            return null;
        }

        if (returnType == Boolean.TYPE) {
            return false;
        }

        if (returnType == Integer.TYPE || returnType == Long.TYPE || returnType == Short.TYPE || returnType == Byte.TYPE) {
            return 0;
        }

        if (returnType == Float.TYPE || returnType == Double.TYPE) {
            return 0.0;
        }

        if (returnType == Character.TYPE) {
            return '\0';
        }

        if (args != null && args.length > 0 && returnType.isInstance(args[0])) {
            return args[0];
        }

        return null;
    }
}
