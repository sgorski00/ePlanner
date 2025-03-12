package pl.sgorski.EPlanner.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
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
    private String name;

    private String description;

    private Timestamp createdAt;
}
