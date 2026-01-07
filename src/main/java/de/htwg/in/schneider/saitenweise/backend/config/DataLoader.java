package de.htwg.in.schneider.saitenweise.backend.config;

import de.htwg.in.schneider.saitenweise.backend.model.Role;
import de.htwg.in.schneider.saitenweise.backend.model.User;
import de.htwg.in.schneider.saitenweise.backend.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    private final UserRepository userRepository;

    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {

    
        String adminOauthId = "github|164401690";

        userRepository.findByOauthId(adminOauthId)
            .orElseGet(() -> {
                User admin = new User();
                admin.setOauthId(adminOauthId);
                admin.setEmail("admin@projectifypro.local");
                admin.setName("Admin");
                admin.setRole(Role.ADMIN);
                return userRepository.save(admin);
            });
    }
}
