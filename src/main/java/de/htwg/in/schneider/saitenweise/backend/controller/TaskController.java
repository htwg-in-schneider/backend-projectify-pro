package de.htwg.in.schneider.saitenweise.backend.controller;

import de.htwg.in.schneider.saitenweise.backend.model.Task;
import de.htwg.in.schneider.saitenweise.backend.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@CrossOrigin(origins = "http://localhost:5173") // Frontend url
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    
    @GetMapping
    public ResponseEntity<List<Task>> getTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status) {

        // Kein Filter gesetzt
        if ((title == null || title.isBlank()) && (status == null || status.isBlank())) {
            return ResponseEntity.ok(taskRepository.findAll());
        }

        // Nur nach Titel suchen
        if (title != null && !title.isBlank() && (status == null || status.isBlank())) {
            return ResponseEntity.ok(taskRepository.findByTitleContainingIgnoreCase(title));
        }

        // Nur nach Status filtern
        if ((title == null || title.isBlank()) && status != null && !status.isBlank()) {
            return ResponseEntity.ok(taskRepository.findByStatus(status));
        }

        // Kombination von beiden
        return ResponseEntity.ok(
                taskRepository.findByTitleContainingIgnoreCaseAndStatus(title, status)
        );
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        task.setId(null); // sicherheitshalber
        Task saved = taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id,
                                           @RequestBody Task updated) {
        return taskRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(updated.getTitle());
                    existing.setUser(updated.getUser());
                    existing.setStartDate(updated.getStartDate());
                    existing.setEndDate(updated.getEndDate());
                    existing.setDuration(updated.getDuration());
                    existing.setStatus(updated.getStatus());
                    Task saved = taskRepository.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
