package com.prog4_tpi_grupo1.backend.auth.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prog4_tpi_grupo1.backend.auth.models.Usuario;



public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email); // para login

    Optional<Usuario> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    //metodo para el ranking
    List<Usuario> findAllByOrderByPuntosTotalesDescPlenosAcertadosDescUsernameAsc();

    //evitar cargar usuario desde el find by id
    @Query("""
                SELECT u
                FROM Usuario u
                LEFT JOIN FETCH u.grupos
                WHERE u.id = :id
            """)
    Optional<Usuario> findByIdConGrupos(@Param("id") Long id);
}
