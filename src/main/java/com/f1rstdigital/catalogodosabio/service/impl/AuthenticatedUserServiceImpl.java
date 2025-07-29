package com.f1rstdigital.catalogodosabio.service.impl;

import com.f1rstdigital.catalogodosabio.config.security.CustomUserDetails;
import com.f1rstdigital.catalogodosabio.domain.user.User;
import com.f1rstdigital.catalogodosabio.service.AuthenticatedUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticatedUserServiceImpl implements AuthenticatedUserService {

    @Override
    public User getAuthenticatedUser() {
        return extractUser(getAuthenticationOrThrow());
    }

    @Override
    public User getAuthenticatedUser(Authentication auth) {
        return extractUser(validateAuthentication(auth));
    }

    @Override
    public UUID getAuthenticatedUserId() {
        return getAuthenticatedUser().getId();
    }

    @Override
    public boolean isAnonymous() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return isInvalid(auth);
    }

    private Authentication getAuthenticationOrThrow() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return validateAuthentication(auth);
    }

    private Authentication validateAuthentication(Authentication auth) {
        if (isInvalid(auth)) {
            throw new IllegalStateException("Unauthenticated user");
        }
        return auth;
    }

    private boolean isInvalid(Authentication auth) {
        return auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal());
    }

    private User extractUser(Authentication auth) {
        if (auth.getPrincipal() instanceof CustomUserDetails(var user)) {
            return user;
        }
        throw new IllegalStateException("Invalid authenticated user");
    }
}
