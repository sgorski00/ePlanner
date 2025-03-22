package pl.sgorski.EPlanner.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.model.EventStatus;
import pl.sgorski.EPlanner.service.UserService;

import java.security.Principal;
import java.util.NoSuchElementException;

@RequestMapping("/profile")
@Controller
public class ProfileController {

    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String show(
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try{
            ApplicationUser user = userService.findByUsername(principal.getName());
            model.addAttribute("user", user);
            model.addAttribute("totalEvents", user.getEventCount(null));
            model.addAttribute("ongoingEvents", user.getEventCount(EventStatus.NOT_COMPLETED));
            model.addAttribute("completedEvents", user.getEventCount(EventStatus.COMPLETED));
            model.addAttribute("archivedEvents", user.getEventCount(EventStatus.ARCHIVED));
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("info", "User not found");
            return "redirect:/";
        }
        return "profile";
    }

    @PostMapping("/change_password")
    public String changePassword(
            @RequestParam("password") String password,
            RedirectAttributes redirectAttributes,
            Principal principal
    ) {
        try {
            ApplicationUser user = userService.findByUsername(principal.getName());
            user.setPassword(password);
            userService.save(user);
        }  catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/";
        }
        redirectAttributes.addFlashAttribute("info", "Password changed successfully");
        return "redirect:/profile";
    }

    @PostMapping("/delete")
    public String delete(
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response,
            Principal principal
    ) {
        try {
            ApplicationUser user = userService.findByUsername(principal.getName());
            userService.delete(user);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
        }  catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/";
        }
        redirectAttributes.addFlashAttribute("info", "User deleted successfully");
        return "redirect:/login";
    }
}
