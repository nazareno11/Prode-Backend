package com.prog4_tpi_grupo1.backend.equipo.repository;

import com.prog4_tpi_grupo1.backend.equipo.entity.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    Optional<Equipo> findByExternalId(Long externalId);

}
