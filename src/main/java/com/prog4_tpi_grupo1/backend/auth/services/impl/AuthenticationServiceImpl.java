package com.prog4_tpi_grupo1.backend.auth.services.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prog4_tpi_grupo1.backend.auth.dtos.request.LoginRequest;
import com.prog4_tpi_grupo1.backend.auth.dtos.request.RegisterRequest;
import com.prog4_tpi_grupo1.backend.auth.dtos.response.AuthResponse;
import com.prog4_tpi_grupo1.backend.auth.models.Rol;
import com.prog4_tpi_grupo1.backend.auth.models.Usuario;
import com.prog4_tpi_grupo1.backend.auth.repositories.IUsuarioRepository;
import com.prog4_tpi_grupo1.backend.auth.services.interfaces.IAuthenticationService;
import com.prog4_tpi_grupo1.backend.auth.services.interfaces.IJwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl
        implements IAuthenticationService {

    private final IUsuarioRepository usuarioRepository;

    private final IJwtService jwtService;

    //ecripta la password
    private final PasswordEncoder passwordEncoder;
    //encripta el usuario        
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RuntimeException("El email ya está registrado");
        }

        if (usuarioRepository.existsByUsername(request.username())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        Usuario usuario = Usuario.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))// emcriptamos la password antes de guardar
                .rol(Rol.USER)
                .puntosTotales(0)
                .plenosAcertados(0)
                .build();

        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario);

        return new AuthResponse(
                token,
                "Bearer",
                usuario.getUsername()
        );
    }

    @Override
    public AuthResponse authenticate(LoginRequest request) {

        authenticationManager.authenticate( // validamos credenciales
                new UsernamePasswordAuthenticationToken(
                        request.email(),    
                        request.password() //creamos un token de auttenticacion con el email y la password
                )
        );

        Usuario usuario = usuarioRepository // validamos si existe el usuario 
                .findByEmail(request.email())
                .orElseThrow();

        String token = jwtService.generateToken(usuario); // token del usuario 

        return new AuthResponse(     //delvolvemos el token en un objeto authresponse
                token,
                "Bearer",
                usuario.getUsername()
        );
    }
}
