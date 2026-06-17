package com.prog4_tpi_grupo1.backend.fecha.repository;

import com.prog4_tpi_grupo1.backend.fecha.entity.EstadoFecha;
import com.prog4_tpi_grupo1.backend.fecha.entity.Fecha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FechaRepository
        extends JpaRepository<Fecha, Long> {

    Optional<Fecha> findByGrupoAndMatchday(
            String grupo,
            Integer matchday
    );

    List<Fecha> findByEstado(EstadoFecha estado);
}
