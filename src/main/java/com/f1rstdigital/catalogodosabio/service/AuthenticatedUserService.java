package com.f1rstdigital.catalogodosabio.service;

import com.f1rstdigital.catalogodosabio.domain.user.User;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface AuthenticatedUserService {
    User getAuthenticatedUser();

    User getAuthenticatedUser(Authentication auth);

    UUID getAuthenticatedUserId();

    boolean isAnonymous();
}

