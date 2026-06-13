package com.prog4_tpi_grupo1.backend.auth.services.impl;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.prog4_tpi_grupo1.backend.auth.models.Usuario;
import com.prog4_tpi_grupo1.backend.auth.services.interfaces.IJwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements IJwtService {

    private static final String SECRET_KEY =
            "mi_clave_super_secreta_para_jwt_debe_tener_al_menos_32_caracteres";

    @Override
    public String generateToken(UserDetails user) {
        Usuario usuario = (Usuario) user; // cast para que guarde el email como subject y no el ussername
        return Jwts.builder()
                .subject(usuario.getEmail()) //subject del token
                .issuedAt(new Date()) // fecha de emision
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 24 horas expiracion 
                .signWith(getSigningKey()) // firma del token
                .compact(); 
    }

    @Override
    public String extractUsername(String token) {
        return getClaims(token).getSubject(); 
    }

    @Override
    public boolean isTokenValid(String token, UserDetails user) {
        final String username = extractUsername(token);

        return username.equals(user.getUsername()) // verifica si el username es igual y si expiro
                && !isTokenExpired(token);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()    //parsea el token y verifica la firma y obtiene los claims 
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private SecretKey getSigningKey() { // metodo privado para obtener la clave y asi no hardcodearla
        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes()
        );
    }
}
