package pl.sgorski.EPlanner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.sgorski.EPlanner.model.Event;
import pl.sgorski.EPlanner.service.EventService;
import pl.sgorski.EPlanner.utils.DateUtils;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventsController {

    private final EventService eventService;

    @Autowired
    public EventsController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String show(
            @RequestParam(value = "dateFrom", required = false) String dateFromStr,
            @RequestParam(value = "dateTo", required = false) String dateToStr,
            Model model
    ) {
        LocalDate dateFrom, dateTo;
        try{
            dateFrom = LocalDate.parse(dateFromStr);
            dateTo = LocalDate.parse(dateToStr);
        } catch (DateTimeParseException | NullPointerException e) {
            dateFrom = LocalDate.now().withDayOfMonth(1);
            dateTo = DateUtils.getLastDayOfMonth(LocalDate.now());
        }
        List<Event> events = eventService.getAllEventsBetween(dateFrom, dateTo);
        model.addAttribute("events", events);
        model.addAttribute("dayFrom", dateFrom);
        model.addAttribute("dayTo", dateTo);
        return "events";
    }
}
