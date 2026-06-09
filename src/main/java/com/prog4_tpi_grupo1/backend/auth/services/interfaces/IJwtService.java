package com.prog4_tpi_grupo1.backend.auth.services.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

/*
quiero recibir el usuario authenticado y generar el token (jwt)

leemos el jwt y extraemos el username de una peticion protegida

y verificamos si el token es valido (firma correcta, token no expirado, pertenece al usuario apropiado)
*/
public interface IJwtService {

    String generateToken(UserDetails user);

    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails user);

}