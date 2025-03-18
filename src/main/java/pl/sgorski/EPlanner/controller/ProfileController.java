package pl.sgorski.EPlanner.controller;

import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.service.UserService;

import java.security.Principal;
import java.util.NoSuchElementException;

@RequestMapping("/profile")
@Controller
public class ProfileController {

    private static final Logger log = LogManager.getLogger(ProfileController.class);
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
            model.addAttribute("eventsSize", user.getEvents().size());
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
}
