package com.prog4_tpi_grupo1.backend.auth.services.interfaces;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserDetailsService extends UserDetailsService {
    UserDetails loadUserByUsername(String email);
}
