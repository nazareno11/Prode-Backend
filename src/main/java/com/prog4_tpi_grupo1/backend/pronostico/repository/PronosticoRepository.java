package com.prog4_tpi_grupo1.backend.pronostico.repository;

import com.prog4_tpi_grupo1.backend.auth.models.Usuario;
import com.prog4_tpi_grupo1.backend.partido.entity.EstadoPartido; // <-- Importamos el Enum
import com.prog4_tpi_grupo1.backend.pronostico.entity.Pronostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PronosticoRepository extends JpaRepository<Pronostico, Long> {

    Optional<Pronostico> findByUsuarioIdAndPartidoId(Long usuarioId, Long partidoId);

    @Query("SELECT p FROM Pronostico p WHERE p.usuario.id = :usuarioId AND (:estado IS NULL OR p.partido.estado = :estado)")
    List<Pronostico> findByUsuarioIdAndPartidoEstado(
            @Param("usuarioId") Long usuarioId,
            @Param("estado") EstadoPartido estado);

    List<Pronostico> findByPartidoIdAndUsuarioIdNot(Long partidoId, Long usuarioId);

    List<Pronostico> findByPartidoId(Long partidoId);

    long countByUsuario(Usuario usuario);
}

