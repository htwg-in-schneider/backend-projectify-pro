package de.htwg.in.schneider.saitenweise.backend.controller;

import de.htwg.in.schneider.saitenweise.backend.model.User;
import de.htwg.in.schneider.saitenweise.backend.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Erfüllt Aufgabe 5b: Administrator kann alle Stammdaten (Nutzer) einsehen.
     * @PreAuthorize stellt sicher, dass nur Nutzer mit der Rolle 'ADMIN' diesen Endpunkt aufrufen können.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}