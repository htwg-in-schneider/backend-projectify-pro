package de.htwg.in.schneider.saitenweise.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "project")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Der Projektname darf nicht leer sein.")
    @Size(min = 3, max = 50, message = "Der Name muss zwischen 1 und 50 Zeichen lang sein.")
    private String name;

    @NotBlank(message = "Ein Status ist erforderlich.")
    private String status;

    private String startDate;  
    private String endDate;

    @NotBlank(message = "Bitte geben Sie eine geplante Dauer an.")
    private String duration;

    public Project() {
    }

    public Project(String name, String status) {
        this.name = name;
        this.status = status;
    }

    // Getter und Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
}