package pl.sgorski.EPlanner.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.service.UserService;

import java.util.Arrays;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String show(Model model) {
        model.addAttribute("user", new ApplicationUser());
        return "register";
    }

    @PostMapping
    public String register(
            @Valid @ModelAttribute ApplicationUser user,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return "redirect:/register";
        }

        userService.save(user);
        redirectAttributes.addFlashAttribute("info", "User successfully registered");
        return "redirect:/login";
    }
}
