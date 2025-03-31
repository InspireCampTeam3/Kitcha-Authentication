package com.kitcha.authentication.Utils;

import com.kitcha.authentication.entity.UserEntity;
import com.kitcha.authentication.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final String testUserPassword;
    private final String adminPassword;

    public DataInitializer(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, Environment env) {
        this.userRepository = userRepository;
        this.testUserPassword = bCryptPasswordEncoder.encode(Objects.requireNonNull(env.getProperty("TEST_USER_PASSWORD")));
        this.adminPassword = bCryptPasswordEncoder.encode(Objects.requireNonNull(env.getProperty("TEST_ADMIN_PASSWORD")));
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            UserEntity user = new UserEntity();
            user.setNickname("testUser");
            user.setEmail("test@test.com");
            user.setPassword(testUserPassword);
            user.setRole("USER");
            UserEntity admin = new UserEntity();
            admin.setNickname("admin");
            admin.setEmail("admin@kitcha.shop");
            admin.setPassword(adminPassword);
            admin.setRole("ADMIN");
            userRepository.save(user);
            userRepository.save(admin);
        }
    }
}
