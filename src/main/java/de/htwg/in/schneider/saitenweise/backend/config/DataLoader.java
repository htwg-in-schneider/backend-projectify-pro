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
                new UserSeed("Katarina (Admin)", "katarina@projectify.local", "github|164401690", Role.ADMIN),
                new UserSeed("Julian (Admin)", "julian@projectify.local", "github|183802963", Role.ADMIN)
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

                new UserSeed("Goofy Müller", "goofy@projectify.local", "auth0|69651b568e6c51fcb01347ae", Role.REGULAR),
                new UserSeed("Moritz Flitzer", "moritz@projectify.local", "auth0|696525a79e1902936ced391a", Role.REGULAR),
                new UserSeed("Alexa Schmidt", "alexa@projectify.local", "auth0|696526199e1902936ced3992", Role.REGULAR),
                new UserSeed("Maria Kunst", "maria@projectify.local", "auth0|696526fc9e1902936ced3a59", Role.REGULAR)

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