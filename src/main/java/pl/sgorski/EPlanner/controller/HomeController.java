package pl.sgorski.EPlanner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.model.Event;
import pl.sgorski.EPlanner.service.EventService;
import pl.sgorski.EPlanner.service.UserService;

import java.security.Principal;

@RequestMapping("/")
@Controller
public class HomeController {

    private final UserService userService;
    private final EventService eventService;

    @Autowired
    public HomeController(UserService userService, EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    @GetMapping
    public String home(
            Model model,
            Principal principal
    ) {
        ApplicationUser loggedUser = userService.findByUsername(principal.getName());

        Event upcomingEvent = eventService.getUpcomingEvent(loggedUser);
        model.addAttribute("upcomingEvent", upcomingEvent);
        model.addAttribute("totalEvents", loggedUser.getEventCount(null));
        return "home";
    }
}
