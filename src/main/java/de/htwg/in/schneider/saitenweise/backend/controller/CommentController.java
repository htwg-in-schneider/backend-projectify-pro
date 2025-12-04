package de.htwg.in.schneider.saitenweise.backend.controller;

import de.htwg.in.schneider.saitenweise.backend.model.Comment;
import de.htwg.in.schneider.saitenweise.backend.model.Task;
import de.htwg.in.schneider.saitenweise.backend.repository.CommentRepository;
import de.htwg.in.schneider.saitenweise.backend.repository.TaskRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private static final Logger LOG = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Comment> getAllComments() {
        LOG.info("Fetching all comments");
        return commentRepository.findAll();
    }


    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Comment>> getCommentsByTask(@PathVariable Long taskId) {

        LOG.info("Fetching comments for task id {}", taskId);

        if (!taskRepository.existsById(taskId)) {
            LOG.warn("Task with id {} not found", taskId);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(commentRepository.findByTaskId(taskId));
    }


    @PostMapping("/task/{taskId}")
    public ResponseEntity<?> createCommentForTask(
            @PathVariable Long taskId,
            @RequestBody Comment comment) {

        LOG.info("Creating comment for task {}", taskId);

        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isEmpty()) {
            LOG.warn("Task {} not found", taskId);
            return ResponseEntity.notFound().build();
        }

        Task task = taskOpt.get();
        comment.setTask(task);

        Comment saved = commentRepository.save(comment);
        LOG.info("Created comment {} for task {}", saved.getId(), taskId);

        return ResponseEntity.status(201).body(saved);
    }


/*     @PostMapping
    public ResponseEntity<?> createComment(@RequestBody Comment comment) {

        LOG.info("Attempting to create comment for task id {}",
                (comment.getTask() != null ? comment.getTask().getId() : null));

        // Check if task info is missing
        if (comment.getTask() == null || comment.getTask().getId() == null) {
            LOG.warn("Comment task reference is null or has no id");
            return ResponseEntity.badRequest().build();
        }

        // Lookup Task
        Optional<Task> taskOpt = taskRepository.findById(comment.getTask().getId());
        if (taskOpt.isEmpty()) {
            LOG.warn("Task not found for comment creation: {}", comment.getTask().getId());
            return ResponseEntity.badRequest().build();
        }

        // Assign parent task
        Task task = taskOpt.get();
        comment.setTask(task);

        // Save
        Comment saved = commentRepository.save(comment);
        LOG.info("Created comment with id {}", saved.getId());

        return ResponseEntity.ok(saved);
    } */




    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {

        LOG.info("Attempting to delete comment with id {}", id);

        Optional<Comment> commentOpt = commentRepository.findById(id);

        if (commentOpt.isPresent()) {
            commentRepository.delete(commentOpt.get());
            LOG.info("Deleted comment with id {}", id);
            return ResponseEntity.noContent().build();
        } else {
            LOG.warn("Comment not found for deletion: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
