package com.prog4_tpi_grupo1.backend.grupo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prog4_tpi_grupo1.backend.grupo.models.Grupo;

public interface IGrupoRepository extends JpaRepository<Grupo, Long> {

    Optional<Grupo> findByCodigoInvitacion(String codigoInvitacion);

    boolean existsByCodigoInvitacion(String codigoInvitacion);


}
