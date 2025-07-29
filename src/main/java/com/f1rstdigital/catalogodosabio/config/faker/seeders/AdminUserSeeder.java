package com.f1rstdigital.catalogodosabio.config.faker.seeders;

import com.f1rstdigital.catalogodosabio.domain.role.Role;
import com.f1rstdigital.catalogodosabio.domain.user.User;
import com.f1rstdigital.catalogodosabio.repository.RoleRepository;
import com.f1rstdigital.catalogodosabio.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Profile("local")
public class AdminUserSeeder {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserSeeder.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.user.email:admin@admin.com}")
    private String adminEmail;

    @Value("${admin.user.password:admin123}")
    private String adminPassword;

    @Value("${admin.user.name:Admin}")
    private String adminName;

    public AdminUserSeeder(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void seedAdminUser() {
        Optional<User> existingAdmin = userRepository.findByEmail(adminEmail);

        if (existingAdmin.isEmpty()) {
            Optional<Role> adminRoleOpt = roleRepository.findByName("ROLE_ADMIN");
            if (adminRoleOpt.isEmpty()) {
                logger.warn("Admin role 'ROLE_ADMIN' not found. Skipping admin user creation.");
                return;
            }

            Role adminRole = adminRoleOpt.get();

            User admin = new User();
            admin.setName(adminName);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.addRole(adminRole);

            userRepository.save(admin);

            logger.info("Admin user created: {} / {}", adminEmail, adminPassword);
        } else {
            logger.info("ℹAdmin user already exists. Skipping seeding.");
        }
    }
}
