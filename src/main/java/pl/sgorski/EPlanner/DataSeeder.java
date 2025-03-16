package pl.sgorski.EPlanner;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.model.Event;
import pl.sgorski.EPlanner.service.EventService;
import pl.sgorski.EPlanner.service.UserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    private final EventService eventService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final Faker faker;
    private final Random random;

    @Autowired
    public DataSeeder(EventService eventService, UserService userService, PasswordEncoder passwordEncoder) {
        this.eventService = eventService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        faker = new Faker();
        random = new Random();
    }

    @Override
    public void run(String... args) throws Exception {
        List<ApplicationUser> users = seedUsers();
        seedEvents(users);
    }

    private List<ApplicationUser> seedUsers() {
        List<ApplicationUser> users = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            ApplicationUser user = new ApplicationUser();
            user.setUsername(faker.name().username());
            user.setEmail(faker.internet().emailAddress());
            user.setProvider(random.nextBoolean() ? "LOCAL" : "GOOGLE");
            if(user.getProvider().equals("LOCAL")) {
                user.setPassword(passwordEncoder.encode("password"));
            } else {
                user.setProviderId(faker.internet().uuid());
            }
            users.add(user);
            userService.save(user);
        }
        return users;
    }

    private void seedEvents(List<ApplicationUser> users) {
        for(int i = 0; i<100; i++) {
            Event event = new Event();
            event.setUser(users.get(random.nextInt(users.size())));
            event.setCreatedAt(Timestamp.from(Instant.now()));
            event.setName(faker.name().title());
            event.setDescription(faker.weather().description());
            event.setHour(LocalTime.of(random.nextInt(0,24), 0));
            event.setDay(LocalDate.of(random.nextInt(2025,2026), random.nextInt(1,4), random.nextInt(1,29)));
            eventService.save(event);
        }
    }
}
