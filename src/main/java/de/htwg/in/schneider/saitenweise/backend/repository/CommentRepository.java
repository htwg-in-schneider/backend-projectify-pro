package de.htwg.in.schneider.saitenweise.backend.repository;

import de.htwg.in.schneider.saitenweise.backend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTaskId(Long taskId);
}
