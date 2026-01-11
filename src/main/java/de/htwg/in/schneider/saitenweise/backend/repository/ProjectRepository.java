package de.htwg.in.schneider.saitenweise.backend.repository;

import de.htwg.in.schneider.saitenweise.backend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Hier können später Methoden wie findByName etc. hinzugefügt werden
}