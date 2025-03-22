package pl.sgorski.EPlanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.model.Event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("""
        SELECT e FROM Event e
        WHERE e.user = :user
        AND (
            (e.day = :date AND e.hour > :time)
            OR e.day > :date
        )
        ORDER BY e.day ASC, e.hour ASC
        LIMIT 1
""")
    Optional<Event> findClosestEvent(@Param("user") ApplicationUser user, @Param("date") LocalDate date, @Param("time") LocalTime hour);
    List<Event> findAllByDayBetweenAndUserOrderByDayAsc(LocalDate dayFrom, LocalDate dayTo, ApplicationUser user);
}
