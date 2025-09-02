package com.oracle.config;

import com.oracle.entity.AppUser;
import com.oracle.entity.Role;
import com.oracle.repository.RoleRepository;
import com.oracle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(UUID.randomUUID().toString(), "ROLE_ADMIN")));
        roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role(UUID.randomUUID().toString(), "ROLE_USER")));
        if (userRepository.findByUsername("admin").isEmpty()) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
        }
    }
}
