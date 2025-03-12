package pl.sgorski.EPlanner;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.sgorski.EPlanner.model.Event;
import pl.sgorski.EPlanner.service.EventService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    private final EventService eventService;
    private final Faker faker;
    private final Random random;

    @Autowired
    public DataSeeder(EventService eventService) {
        this.eventService = eventService;
        faker = new Faker();
        random = new Random();
    }

    @Override
    public void run(String... args) throws Exception {
        for(int i = 0; i<100; i++) {
            Event event = new Event();
            event.setCreatedAt(Timestamp.from(Instant.now()));
            event.setName(faker.name().title());
            event.setDescription(faker.weather().description());
            event.setHour(LocalTime.of(random.nextInt(0,24), 0));
            event.setDay(LocalDate.of(random.nextInt(2023,2026), random.nextInt(1,13), random.nextInt(1,29)));
            eventService.save(event);
        }
    }
}
