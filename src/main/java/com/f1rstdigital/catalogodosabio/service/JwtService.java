package com.f1rstdigital.catalogodosabio.service;

public interface JwtService {
    String generateToken(com.f1rstdigital.catalogodosabio.domain.user.User user);

    boolean isTokenValid(String token);

    String getEmailFromToken(String token);
}
