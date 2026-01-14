package de.htwg.in.schneider.saitenweise.backend.config;

import java.util.Arrays;
import java.util.List;
import java.util.Optional; // Import hinzufügen

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
            
            // --- Admins ---
            List<UserSeed> priorityUsers = List.of(
                new UserSeed("Katarina (Admin)", "katarina@projectify.local", "github|164401690", Role.ADMIN),
                new UserSeed("Julian (Admin)", "julian@projectify.local", "github|183802963", Role.ADMIN)
            );

            for (UserSeed seed : priorityUsers) {
                createOrUpdateUser(userRepository, seed); // Neue Methode aufrufen
            }

            // --- 2. Admins aus application.properties ---
            if (adminOauthIds != null && !adminOauthIds.isBlank()) {
                Arrays.stream(adminOauthIds.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .forEach(oauthId -> {
                        // Hier belassen wir es oft beim 'createIfMissing', 
                        // oder du passt es auch an, wenn du willst.
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

            // --- 3. Mitarbeiter laden ---
            List<UserSeed> staffMembers = List.of(
                new UserSeed("Goofy Müller", "goofy@projectify.local", "auth0|69651b568e6c51fcb01347ae", Role.REGULAR),
                new UserSeed("Moritz Flitzer", "moritz@projectify.local", "auth0|696525a79e1902936ced391a", Role.REGULAR),
                new UserSeed("Alexa Schmidt", "alexa@projectify.local", "auth0|696526199e1902936ced3992", Role.REGULAR),
                new UserSeed("Maria Kunst", "maria@projectify.local", "auth0|696526fc9e1902936ced3a59", Role.REGULAR)
            );

            for (UserSeed staff : staffMembers) {
                createOrUpdateUser(userRepository, staff); // Neue Methode aufrufen
            }
        };
    }

    // WICHTIG: Die angepasste Logik
    private void createOrUpdateUser(UserRepository repo, UserSeed seed) {
        Optional<User> existingOpt = repo.findByOauthId(seed.oauthId);

        if (existingOpt.isEmpty()) {
            // FALL 1: Nutzer existiert noch nicht -> Neu anlegen
            User user = new User();
            user.setOauthId(seed.oauthId);
            user.setName(seed.name);
            user.setEmail(seed.email);
            user.setRole(seed.role);
            repo.save(user);
            System.out.println("User angelegt: " + seed.name);
        } else {
            // FALL 2: Nutzer existiert schon -> Daten aktualisieren (Sync)
            User user = existingOpt.get();
            boolean changed = false;

            // Prüfen, ob sich was geändert hat, um unnötige DB-Writes zu sparen
            if (!user.getName().equals(seed.name)) {
                user.setName(seed.name);
                changed = true;
            }
            if (!user.getEmail().equals(seed.email)) {
                user.setEmail(seed.email);
                changed = true;
            }
            if (!user.getRole().equals(seed.role)) {
                user.setRole(seed.role);
                changed = true;
            }

            if (changed) {
                repo.save(user);
                System.out.println("User aktualisiert: " + seed.name);
            }
        }
    }

    record UserSeed(String name, String email, String oauthId, Role role) {}
}