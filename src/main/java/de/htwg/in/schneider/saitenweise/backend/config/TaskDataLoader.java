package de.htwg.in.schneider.saitenweise.backend.config;

import de.htwg.in.schneider.saitenweise.backend.repository.TaskRepository;
import de.htwg.in.schneider.saitenweise.backend.repository.CommentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskDataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskDataLoader.class);

    @Bean
    public CommandLineRunner loadData(TaskRepository taskRepository, CommentRepository commentRepository) {
        return args -> {           
            LOGGER.info("TaskDataLoader aktiv: Keine initialen Testdaten werden geladen.");
        };
    }
}