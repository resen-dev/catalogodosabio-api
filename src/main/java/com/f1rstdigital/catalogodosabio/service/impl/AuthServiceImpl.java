package com.f1rstdigital.catalogodosabio.service.impl;

import com.f1rstdigital.catalogodosabio.domain.role.Role;
import com.f1rstdigital.catalogodosabio.domain.user.User;
import com.f1rstdigital.catalogodosabio.dto.auth.AuthResponseDto;
import com.f1rstdigital.catalogodosabio.dto.auth.LoginRequestDto;
import com.f1rstdigital.catalogodosabio.dto.auth.RegisterRequestDto;
import com.f1rstdigital.catalogodosabio.handler.exceptions.EmailAlreadyExistsException;
import com.f1rstdigital.catalogodosabio.handler.exceptions.RoleNotFoundException;
import com.f1rstdigital.catalogodosabio.repository.RoleRepository;
import com.f1rstdigital.catalogodosabio.repository.UserRepository;
import com.f1rstdigital.catalogodosabio.service.AuthService;
import com.f1rstdigital.catalogodosabio.service.AuthenticatedUserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticatedUserService authenticatedUserService;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtServiceImpl jwtService,
            AuthenticationManager authenticationManager,
            AuthenticatedUserService authenticatedUserService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.authenticatedUserService = authenticatedUserService;
    }

    public AuthResponseDto register(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException();
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("ROLE_USER"));

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.addRole(userRole);

        userRepository.save(user);

        String jwt = jwtService.generateToken(user);
        return new AuthResponseDto(jwt);
    }

    public AuthResponseDto login(LoginRequestDto request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = authenticatedUserService.getAuthenticatedUser(auth);

        String jwt = jwtService.generateToken(user);
        return new AuthResponseDto(jwt);
    }
}
