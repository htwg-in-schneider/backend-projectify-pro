package de.htwg.in.schneider.saitenweise.backend.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import de.htwg.in.schneider.saitenweise.backend.model.Role;
import de.htwg.in.schneider.saitenweise.backend.model.User;
import de.htwg.in.schneider.saitenweise.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public Map<String, String> profile(@AuthenticationPrincipal Jwt jwt) {
        String oauthId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("name");

        User user = userRepository.findByOauthId(oauthId)
            .map(existing -> {
                // Bei jedem Login werden E-Mail und Name kurz synchronisiert, falls vorhanden
                if (email != null && existing.getEmail().contains("@unknown.local")) existing.setEmail(email);
                return userRepository.save(existing);
            })
            .orElseGet(() -> {
                User u = new User();
                u.setOauthId(oauthId);
                u.setEmail(email != null ? email : (oauthId + "@unknown.local"));
                u.setName(name != null ? name : "New User");
                u.setRole(Role.REGULAR);
                return userRepository.save(u);
            });

        return Map.of(
            "name", user.getName(),
            "email", user.getEmail(),
            "role", user.getRole().name()
        );
    }

    // Endpunkt zum Ändern des Namens
    @PutMapping("/name")
    public ResponseEntity<User> updateName(@AuthenticationPrincipal Jwt jwt, @RequestBody Map<String, String> body) {
        String oauthId = jwt.getSubject();
        String newName = body.get("name");

        if (newName == null || newName.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        return userRepository.findByOauthId(oauthId)
            .map(user -> {
                user.setName(newName);
                return ResponseEntity.ok(userRepository.save(user));
            })
            .orElse(ResponseEntity.notFound().build());
    }
}