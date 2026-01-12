package de.htwg.in.schneider.saitenweise.backend.controller;

import de.htwg.in.schneider.saitenweise.backend.model.Role; // WICHTIG: Role importieren
import de.htwg.in.schneider.saitenweise.backend.model.User;
import de.htwg.in.schneider.saitenweise.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        // Lade alle, aber filtere ADMINs raus
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() != Role.ADMIN)
                .collect(Collectors.toList());
    }
}