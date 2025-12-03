package de.htwg.in.schneider.saitenweise.backend.config;

import de.htwg.in.schneider.saitenweise.backend.model.Task;
import de.htwg.in.schneider.saitenweise.backend.model.Comment;
import de.htwg.in.schneider.saitenweise.backend.repository.TaskRepository;
import de.htwg.in.schneider.saitenweise.backend.repository.CommentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class TaskDataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskDataLoader.class);

    @Bean
    public CommandLineRunner loadData(TaskRepository taskRepository, CommentRepository commentRepository) {
        return args -> {
            if (taskRepository.count() == 0) {
                LOGGER.info("Database is empty. Loading initial data...");
                loadInitialData(taskRepository, commentRepository);
            } else {
                LOGGER.info("Database already contains data. Skipping data loading.");
            }
        };
    }

    private void loadInitialData(TaskRepository taskRepository, CommentRepository commentRepository) {

        Task t1 = new Task("Dokumentation schreiben", "Lucas",
                "2025-10-01", "2025-10-05", "5", "Erledigt");

        Task t2 = new Task("Mockup erstellen", "Marie",
                "2025-10-03", "2025-10-11", "9", "Erledigt");

        Task t3 = new Task("Frontend-Login implementieren", "Lucas",
                "2025-10-01", "2025-10-07", "7", "In Bearbeitung");

        Task t4 = new Task("Datenbank Schema entwerfen", "Lucas",
                "2025-10-04", "2025-10-07", "3", "Review");

        // zuerst Tasks speichern
        taskRepository.saveAll(Arrays.asList(t1, t2, t3, t4));

        
        Comment c1 = new Comment();
        c1.setUserName("Anna");
        c1.setText("Bitte noch verbessern");
        c1.setTask(t1);

        Comment c2 = new Comment();
        c2.setUserName("Tom");
        c2.setText("Sieht gut aus");
        c2.setTask(t1);

        Comment c3 = new Comment();
        c3.setUserName("Chris");
        c3.setText("Review offen, aber schon weit fortgeschritten");
        c3.setTask(t4);

        // speichern
        commentRepository.saveAll(Arrays.asList(c1, c2, c3));

        LOGGER.info("Initial data loaded successfully.");
    }
}
