package de.htwg.in.schneider.saitenweise.backend.config;

import java.util.Arrays;
import java.util.List;

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
            
            // --- 1. Spezifische Admins (PRIO 1) ---
            List<UserSeed> priorityUsers = List.of(
                new UserSeed("Admin", "github|183802963@unknown.local", "github|183802963", Role.ADMIN),
                new UserSeed("Super Admin", "admin@projectify.pro", "github|164401690", Role.ADMIN)
            );

            for (UserSeed seed : priorityUsers) {
                createUserIfMissing(userRepository, seed);
            }

            // --- 2. Admins aus application.properties (PRIO 2) ---
            if (adminOauthIds != null && !adminOauthIds.isBlank()) {
                Arrays.stream(adminOauthIds.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .forEach(oauthId -> {
                        // Nur anlegen, wenn noch nicht existiert (verhindert Duplikate mit Prio 1)
                        if (userRepository.findByOauthId(oauthId).isEmpty()) {
                            User created = new User();
                            created.setOauthId(oauthId);
                            created.setEmail(oauthId + "@seed.admin");
                            created.setName("Seeded Admin");
                            created.setRole(Role.ADMIN);
                            userRepository.save(created);
                            System.out.println("Config Admin angelegt: " + oauthId);
                        }
                    });
            }

            // --- 3. Mitarbeiter laden (PRIO 3) ---
            List<UserSeed> staffMembers = List.of(

                new UserSeed("Goofy", "goofy@projectify.local", "seed-id-goofy", Role.REGULAR),
                new UserSeed("Max", "max@projectify.local", "seed-id-max", Role.REGULAR),
                new UserSeed("Moritz", "moritz@projectify.local", "seed-id-moritz", Role.REGULAR),
                new UserSeed("Siri", "siri@projectify.local", "seed-id-siri", Role.REGULAR),
                new UserSeed("Alexa", "alexa@projectify.local", "seed-id-alexa", Role.REGULAR)

            );

            for (UserSeed staff : staffMembers) {
                createUserIfMissing(userRepository, staff);
            }
        };
    }

    private void createUserIfMissing(UserRepository repo, UserSeed seed) {
        if (repo.findByOauthId(seed.oauthId).isEmpty()) {
            User user = new User();
            user.setOauthId(seed.oauthId);
            user.setName(seed.name);
            user.setEmail(seed.email);
            user.setRole(seed.role);
            repo.save(user);
            System.out.println("User angelegt: " + seed.name + " (" + seed.role + ")");
        }
    }

    // Record angepasst
    record UserSeed(String name, String email, String oauthId, Role role) {}
}