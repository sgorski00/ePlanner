package pl.sgorski.EPlanner.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sgorski.EPlanner.model.Event;
import pl.sgorski.EPlanner.service.EventService;
import pl.sgorski.EPlanner.utils.DateUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;

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
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);
        return "events";
    }

    @GetMapping("/{id}")
    public String showEvent(
            @PathVariable("id") String id,
            Model model,
            RedirectAttributes redirectAttributes
    ){
        try {
            Long idLong = Long.parseLong(id);
            Event event = eventService.getById(idLong);
            model.addAttribute("event", event);
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/events";
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "Id must be number");
            return "redirect:/events";
        }
        return "event";
    }

    @GetMapping("/add")
    public String showAddEventForm(
            Model model
    ){
        model.addAttribute("event", new Event());
        model.addAttribute("today", LocalDate.now());
        return "add_event";
    }

    @PostMapping("/add")
    public String addEvent(
            @Valid @ModelAttribute("event") Event event,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", "Something went wrong.");
            return "redirect:/events/add";
        }

        eventService.save(event);
        redirectAttributes.addFlashAttribute("info", "Event added succesfully");
        return "redirect:/events/add";
    }
}
