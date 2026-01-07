package de.htwg.in.schneider.saitenweise.backend.controller;

import de.htwg.in.schneider.saitenweise.backend.model.Role;
import de.htwg.in.schneider.saitenweise.backend.model.User;
import de.htwg.in.schneider.saitenweise.backend.repository.UserRepository;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public User getProfile(@AuthenticationPrincipal Jwt jwt) {

        String oauthId = jwt.getSubject();          // Auth0-ID (sub)
        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("name");

        return userRepository.findByOauthId(oauthId)
            .orElseGet(() -> {
                User user = new User();
                user.setOauthId(oauthId);
                user.setEmail(email);
                user.setName(name);
                user.setRole(Role.REGULAR); // Default-Rolle
                return userRepository.save(user);
            });
    }
}
