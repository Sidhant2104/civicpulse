package com.sidhant.civicpulse.config;

import com.sidhant.civicpulse.model.Role;
import com.sidhant.civicpulse.model.User;
import com.sidhant.civicpulse.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class AdminSeeder implements CommandLineRunner {

    private static final Logger logger =
            LoggerFactory.getLogger(AdminSeeder.class);

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(UserRepo userRepo,
                       PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        boolean adminExists = userRepo.existsByRole(Role.ADMIN);

        if(adminExists){
            return;
        }

        User admin = new User();

        admin.setId(UUID.randomUUID().toString());

        admin.setName("System Administrator");

        admin.setEmail("admin@civicpulse.com");

        admin.setPassword(
                passwordEncoder.encode("Admin@123")
        );

        admin.setRole(Role.ADMIN);

        admin.setCreatedAt(LocalDateTime.now());

        userRepo.save(admin);

        logger.info("Default admin created: {}", admin.getEmail());
    }
}