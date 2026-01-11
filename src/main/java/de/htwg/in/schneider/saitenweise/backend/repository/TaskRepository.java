package de.htwg.in.schneider.saitenweise.backend.repository;

import de.htwg.in.schneider.saitenweise.backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByTitleContainingIgnoreCase(String title);

    List<Task> findByStatus(String status);

    List<Task> findByTitleContainingIgnoreCaseAndStatus(String title, String status);

    List<Task> findByProjectId(Long projectId);
}
