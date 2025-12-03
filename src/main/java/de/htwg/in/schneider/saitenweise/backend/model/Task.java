package de.htwg.in.schneider.saitenweise.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "task_user") 
    private String user;
    private String startDate;
    private String endDate;
    private String duration;
    private String status;

    public Task() {
    }

    public Task(String title, String user, String startDate, String endDate,
                String duration, String status) {
        this.title = title;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.status = status;
    }

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
}
