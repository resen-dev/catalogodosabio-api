package com.f1rstdigital.catalogodosabio.service;

import com.f1rstdigital.catalogodosabio.dto.auth.AuthResponseDto;
import com.f1rstdigital.catalogodosabio.dto.auth.LoginRequestDto;
import com.f1rstdigital.catalogodosabio.dto.auth.RegisterRequestDto;

public interface AuthService {
    AuthResponseDto register(RegisterRequestDto request);

    AuthResponseDto login(LoginRequestDto request);
}
