package pl.sgorski.EPlanner.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.model.Event;
import pl.sgorski.EPlanner.service.EventService;
import pl.sgorski.EPlanner.service.UserService;
import pl.sgorski.EPlanner.utils.DateUtils;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/events")
public class EventsController {

    private final EventService eventService;
    private final UserService userService;

    @Autowired
    public EventsController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping
    public String show(
            @RequestParam(value = "dateFrom", required = false) String dateFromStr,
            @RequestParam(value = "dateTo", required = false) String dateToStr,
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        LocalDate dateFrom, dateTo;
        try{
            dateFrom = LocalDate.parse(dateFromStr);
            dateTo = LocalDate.parse(dateToStr);
        } catch (DateTimeParseException | NullPointerException e) {
            dateFrom = LocalDate.now().withDayOfMonth(1);
            dateTo = DateUtils.getLastDayOfMonth(LocalDate.now());
        }

        ApplicationUser user;
        try{
            user = userService.findByUsername(principal.getName());
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("info", "User not found");
            return "redirect:/";
        }
        List<Event> events = eventService.getAllEventsBetweenForUser(dateFrom, dateTo, user);
        model.addAttribute("events", events);
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);
        return "events";
    }

    @GetMapping("/{id}")
    public String showEvent(
            @PathVariable("id") String id,
            Model model,
            RedirectAttributes redirectAttributes,
            Principal principal
    ){
        Event event;
        ApplicationUser user;

        try{
            user = userService.findByUsername(principal.getName());
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("info", "User not found");
            return "redirect:/";
        }

        try {
            Long idLong = Long.parseLong(id);
            event = eventService.getById(idLong);
            if(!event.getUser().equals(user)) {
                throw new IllegalArgumentException("Access denied");
            }
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "Id must be number");
            return "redirect:/events";
        } catch (NoSuchElementException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/events";
        }
        model.addAttribute("event", event);
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
            RedirectAttributes redirectAttributes,
            Principal principal
    ){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", "Something went wrong.");
            return "redirect:/events/add";
        }

        ApplicationUser user;
        try{
            user = userService.findByUsername(principal.getName());
            event.setUser(user);
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/events";
        }

        eventService.save(event);
        redirectAttributes.addFlashAttribute("info", "Event added succesfully");
        return "redirect:/events/add";
    }

    @GetMapping("/edit/{id}")
    public String showAddEventForm(
            @PathVariable("id") String id,
            Model model,
            RedirectAttributes redirectAttributes
    ){
        try{
            Long idLong = Long.parseLong(id);
            Event event = eventService.getById(idLong);
            model.addAttribute("event", event);
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "Id must be number");
            return "redirect:/events";
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/events";
        }
        model.addAttribute("today", LocalDate.now());
        return "edit_event";
    }

    @PostMapping("/edit")
    public String editEvent(
            @Valid @ModelAttribute("event") Event event,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", "Something went wrong.");
            return "redirect:/events/add";
        }

        eventService.save(event);
        redirectAttributes.addFlashAttribute("info", "Event updated succesfully");
        return "redirect:/events";
    }

    @PostMapping("/complete/{id}")
    public String completeEvent(
            @PathVariable("id") Long id,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            ApplicationUser user = userService.findByUsername(principal.getName());
            Event event = eventService.getById(id);
            event.setFinishedAt(Timestamp.from(Instant.now()));
            if(event.getUser().equals(user)) {
                event.setFinishedAt(Timestamp.from(Instant.now()));
                eventService.save(event);
                redirectAttributes.addFlashAttribute("info", "Event completed successfully");
            } else {
                throw new AccessDeniedException("Access denied");
            }
        } catch (NoSuchElementException | AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/events";
        }

        return "redirect:/events/" + id;
    }

    @PostMapping("/archive/{id}")
    public String archiveEvent(
            @PathVariable("id") Long id,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            ApplicationUser user = userService.findByUsername(principal.getName());
            Event event = eventService.getById(id);
            event.setFinishedAt(Timestamp.from(Instant.now()));
            if(event.getUser().equals(user)) {
                event.setArchivedAt(Timestamp.from(Instant.now()));
                eventService.save(event);
                redirectAttributes.addFlashAttribute("info", "Event archived successfully");
            } else {
                throw new AccessDeniedException("Access denied");

            }
        } catch (NoSuchElementException | AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/events";
        }
        return "redirect:/events/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteEvent(
            @PathVariable("id") Long id,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            ApplicationUser user = userService.findByUsername(principal.getName());
            Event event = eventService.getById(id);
            event.setFinishedAt(Timestamp.from(Instant.now()));
            if(event.getUser().equals(user)) {
                eventService.delete(event);
                redirectAttributes.addFlashAttribute("info", "Event deleted successfully");
            } else {
                throw new AccessDeniedException("Access denied");
            }
        } catch (NoSuchElementException | AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/events";
    }
}
