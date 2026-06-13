package com.prog4_tpi_grupo1.backend.auth.services.interfaces;

import com.prog4_tpi_grupo1.backend.auth.dtos.request.LoginRequest;
import com.prog4_tpi_grupo1.backend.auth.dtos.request.RegisterRequest;
import com.prog4_tpi_grupo1.backend.auth.dtos.response.AuthResponse;

public interface IAuthenticationService {

    AuthResponse register(RegisterRequest request);

    AuthResponse authenticate(LoginRequest request); //login 

}