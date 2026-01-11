package de.htwg.in.schneider.saitenweise.backend.controller;

import de.htwg.in.schneider.saitenweise.backend.model.Task;
import de.htwg.in.schneider.saitenweise.backend.model.User;
import de.htwg.in.schneider.saitenweise.backend.model.Role;
import de.htwg.in.schneider.saitenweise.backend.repository.TaskRepository;
import de.htwg.in.schneider.saitenweise.backend.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskController(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    /* ================= HELPER ================= */

    private void requireAdmin(Jwt jwt) {
        String oauthId = jwt.getSubject();
        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        if (user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    /* ================= READ ================= */

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long projectId) {

        if (projectId != null) {
            return ResponseEntity.ok(taskRepository.findByProjectId(projectId));
        }

        if ((title == null || title.isBlank()) && (status == null || status.isBlank())) {
            return ResponseEntity.ok(taskRepository.findAll());
        }

        if (title != null && !title.isBlank() && (status == null || status.isBlank())) {
            return ResponseEntity.ok(taskRepository.findByTitleContainingIgnoreCase(title));
        }

        if ((title == null || title.isBlank()) && status != null && !status.isBlank()) {
            return ResponseEntity.ok(taskRepository.findByStatus(status));
        }

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

    /* ================= WRITE (ADMIN ONLY) ================= */

    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestBody Task task,
            @AuthenticationPrincipal Jwt jwt) {

        requireAdmin(jwt);
        task.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskRepository.save(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @RequestBody Task updated,
            @AuthenticationPrincipal Jwt jwt) {

        requireAdmin(jwt);

        return taskRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(updated.getTitle());
                    existing.setUser(updated.getUser());
                    existing.setStartDate(updated.getStartDate());
                    existing.setEndDate(updated.getEndDate());
                    existing.setDuration(updated.getDuration());
                    existing.setStatus(updated.getStatus());
                    return ResponseEntity.ok(taskRepository.save(existing));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        requireAdmin(jwt);

        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
