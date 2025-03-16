package pl.sgorski.EPlanner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sgorski.EPlanner.model.ApplicationUser;
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
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("info", "User not found");
            return "redirect:/";
        }
        return "profile";
    }
}
