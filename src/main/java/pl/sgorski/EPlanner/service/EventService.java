package pl.sgorski.EPlanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.model.Event;
import pl.sgorski.EPlanner.model.EventStatus;
import pl.sgorski.EPlanner.repository.EventRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEventsBetweenForUser(LocalDate dateFrom, LocalDate dateTo, ApplicationUser user){
        return eventRepository.findAllByDayBetweenAndUserOrderByDayAsc(dateFrom, dateTo, user);
    }

    public List<Event> getAllEventsBetweenForUserWithStatus(LocalDate dateFrom, LocalDate dateTo, ApplicationUser user, EventStatus status) {
        List<Event> allEvents = getAllEventsBetweenForUser(dateFrom, dateTo, user);
        return allEvents.stream()
                .filter(e -> switch (status) {
                    case ARCHIVED -> e.getArchivedAt() != null;
                    case COMPLETED -> e.getFinishedAt() != null;
                    case NOT_COMPLETED -> e.getFinishedAt() == null && e.getArchivedAt() == null;
                    case null -> true;
                })
                .toList();
    }

    public void save(Event event) {
        eventRepository.save(event);
    }

    public Event getById(Long id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Event not found!")
        );
    }

    public void delete(Event event) {
        eventRepository.delete(event);
    }

    public Event getUpcomingEvent(ApplicationUser user) {
        return eventRepository.findClosestEvent(user, LocalDate.now(), LocalTime.now())
                .orElse(null);
    }
}
