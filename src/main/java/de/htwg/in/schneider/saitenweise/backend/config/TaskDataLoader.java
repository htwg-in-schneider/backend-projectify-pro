package de.htwg.in.schneider.saitenweise.backend.config;

import de.htwg.in.schneider.saitenweise.backend.model.Task;
import de.htwg.in.schneider.saitenweise.backend.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskDataLoader {

    @Bean
    public CommandLineRunner loadDemoTasks(TaskRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Task("Dokumentation schreiben", "Lucas",
                        "2025-10-01", "2025-10-05", "5", "Erledigt"));
                repo.save(new Task("Mockup erstellen", "Marie",
                        "2025-10-03", "2025-10-11", "9", "Erledigt"));
                repo.save(new Task("Frontend-Login implementieren", "Lucas",
                        "2025-10-01", "2025-10-07", "7", "In Bearbeitung"));
                repo.save(new Task("Datenbank Schema entwerfen", "Lucas",
                        "2025-10-04", "2025-10-07", "3", "Review"));
            }
        };
    }
}
