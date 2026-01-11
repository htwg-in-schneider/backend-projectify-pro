package de.htwg.in.schneider.saitenweise.backend.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import de.htwg.in.schneider.saitenweise.backend.model.Role;
import de.htwg.in.schneider.saitenweise.backend.model.User;
import de.htwg.in.schneider.saitenweise.backend.repository.UserRepository;

@Configuration
@Profile("!test")
public class DataLoader {

    @Value("${app.admin-oauth-ids:}")
    private String adminOauthIds;

    @Bean
    public CommandLineRunner loadUsers(UserRepository userRepository) {
        return args -> {
            if (adminOauthIds == null || adminOauthIds.isBlank()) return;

            Arrays.stream(adminOauthIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .forEach(oauthId -> {
                    User u = userRepository.findByOauthId(oauthId)
                        .orElseGet(() -> {
                            User created = new User();
                            created.setOauthId(oauthId);
                            created.setEmail(oauthId + "@seed.local");
                            created.setName("Seeded Admin");
                            created.setRole(Role.ADMIN);
                            return created;
                        });

                    u.setRole(Role.ADMIN);
                    userRepository.save(u);
                });
        };
    }
}
