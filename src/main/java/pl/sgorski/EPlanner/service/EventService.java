package pl.sgorski.EPlanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.model.Event;
import pl.sgorski.EPlanner.repository.EventRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EventService {

    private final EventRepository repository;

    @Autowired
    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public List<Event> getAllEventsBetweenForUser(LocalDate dateFrom, LocalDate dateTo, ApplicationUser user){
        return repository.findAllByDayBetweenAndUserOrderByDayAsc(dateFrom, dateTo, user);
    }

    public void save(Event event) {
        repository.save(event);
    }

    public Event getById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Event not found!")
        );
    }

    public void delete(Event event) {
        repository.delete(event);
    }
}
