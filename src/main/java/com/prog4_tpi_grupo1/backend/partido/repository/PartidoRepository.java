package com.prog4_tpi_grupo1.backend.partido.repository;

import com.prog4_tpi_grupo1.backend.partido.entity.Partido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartidoRepository extends JpaRepository<Partido, Long> {

    Optional<Partido> findByExternalId(Long externalId);

    List<Partido> findAllByOrderByFechaHoraAsc();

    List<Partido> findByMatchdayOrderByFechaHoraAsc(Integer matchday);

}
