package de.htwg.in.schneider.saitenweise.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "task")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

  
    @NotBlank(message = "Der Titel der Aufgabe ist ein Pflichtfeld.")
    @Size(min = 3, max = 100, message = "Der Titel muss zwischen 3 und 100 Zeichen lang sein.")
    private String title;

    @Column(name = "task_user")
    private String user;

    private String startDate;
    private String endDate;
    

    @NotBlank(message = "Die Dauer muss angegeben werden.")
    private String duration;


    @NotBlank(message = "Ein Status muss ausgewählt sein.")
    private String status;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;

    @Column(name = "project_id")
    @NotNull(message = "Die Aufgabe muss einem Projekt zugeordnet sein.")
    private Long projectId;
    
    public Task() {

    }

    public Task(String title, String user, String startDate, String endDate,
                String duration, String status, Long projectId) {
        this.title = title;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.status = status;
        this.projectId = projectId;
    }


    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setTask(this);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setTask(null);
    }
}