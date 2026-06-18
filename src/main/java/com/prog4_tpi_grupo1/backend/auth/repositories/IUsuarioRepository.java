package com.prog4_tpi_grupo1.backend.auth.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prog4_tpi_grupo1.backend.auth.models.Usuario;



public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email); // para login

    Optional<Usuario> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    //metodo para el ranking
    List<Usuario> findAllByOrderByPuntosTotalesDescPlenosAcertadosDescUsernameAsc();
}
