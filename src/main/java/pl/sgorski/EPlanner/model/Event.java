package pl.sgorski.EPlanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "user_events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_day", nullable = false)
    private LocalDate day;

    @Column(name = "event_hour",nullable = false)
    private LocalTime hour;

    @Column(nullable = false)
    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String description;

    private Timestamp createdAt;
    private Timestamp lastEdit;
    private Timestamp finishedAt;
    private Timestamp archivedAt;

    @PrePersist
    public void setCreatedAt(){
        if(createdAt == null) {
            this.createdAt = Timestamp.from(Instant.now());
            this.lastEdit = null;
        }
    }

    @PreUpdate
    public void setLastEdit(){
        this.lastEdit = Timestamp.from(Instant.now());
    }
}
