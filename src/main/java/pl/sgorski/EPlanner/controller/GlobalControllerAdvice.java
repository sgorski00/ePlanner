package pl.sgorski.EPlanner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.service.UserService;

import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;

    @Autowired
    public GlobalControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void addUsernameToModel(
            Model model,
            Principal principal
    ) {
        if (principal != null) {
            ApplicationUser user = userService.findByUsername(principal.getName());
            model.addAttribute("gUsername", user.getUsername());
            model.addAttribute("gIsLocal", user.getProvider().equalsIgnoreCase("local"));
        }
    }
}
