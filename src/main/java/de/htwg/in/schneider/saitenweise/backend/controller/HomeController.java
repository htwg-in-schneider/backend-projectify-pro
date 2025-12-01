package de.htwg.in.schneider.saitenweise.backend.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Backend Projectify Pro!";
    }
}