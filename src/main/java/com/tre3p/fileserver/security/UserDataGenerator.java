package com.tre3p.fileserver.security;

import com.tre3p.fileserver.model.Role;
import com.tre3p.fileserver.model.User;
import com.tre3p.fileserver.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Slf4j
public class UserDataGenerator {

    @Value("${security.username}")
    private String username;

    @Value("${security.password}")
    private String password;

    /**
     * Used to authenticate user with his credentials.
     * @param passwordEncoder
     * @param userRepository
     * @return CommandLineRunner
     */
    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        return args -> {
            if (userRepository.count() != 0L) {
                log.info("Using existing database");
                return;
            }
            log.info("Generating demo data");
            log.info("... generating User entity...");

            User user = new User();
            user.setUsername(username);
            user.setHashedPassword(passwordEncoder.encode(password));
            user.setRoles(Collections.singleton(Role.USER));
            userRepository.save(user);

            log.info("Generated demo data");
        };
    }

}
