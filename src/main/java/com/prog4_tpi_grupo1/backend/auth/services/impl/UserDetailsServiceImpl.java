package com.prog4_tpi_grupo1.backend.auth.services.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.prog4_tpi_grupo1.backend.auth.repositories.IUsuarioRepository;
import com.prog4_tpi_grupo1.backend.auth.services.interfaces.IUserDetailsService;

import lombok.RequiredArgsConstructor;

/* buscar usuarios 
Sreing va a recuperar el usuario desde la base de datos cuando spring ejecute 
authenticationManager.authenticate() */

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements IUserDetailsService {

    private final IUsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email)/*aunque se llame loadUserByUsername
                                                        usamos como parametro el email */
            throws UsernameNotFoundException {

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuario no encontrado"
                        ));
    }
}