package pl.sgorski.EPlanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sgorski.EPlanner.model.Event;
import pl.sgorski.EPlanner.repository.EventRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    private final EventRepository repository;

    @Autowired
    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public List<Event> getAllEventsBetween(LocalDate dateFrom, LocalDate dateTo){
        return repository.findAllByDayBetweenOrderByDayAsc(dateFrom, dateTo);
    }

    public void save(Event event) {
        repository.save(event);
    }
}
