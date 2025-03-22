package pl.sgorski.EPlanner.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.model.Role;
import pl.sgorski.EPlanner.service.RoleService;
import pl.sgorski.EPlanner.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LogManager.getLogger(AdminController.class);
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public AdminController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping
    public String showPanel() {
        return "admin_panel";
    }

    @GetMapping("/users")
    public String showUsers(
            @RequestParam(value = "role", required = false) String role,
            Model model
    ) {
        List<ApplicationUser> users;
        try {
            Role choosedRole = roleService.getRoleByName(role);
            users = userService.getUsersWithRole(choosedRole);
            model.addAttribute("role", choosedRole);
        } catch (IllegalArgumentException e) {
            users = userService.getAllUsers();
        }
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/roles")
    public String showRoles(Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        return "roles";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(
            @PathVariable Long id,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        ApplicationUser user = userService.findByUsername(principal.getName());
        if(!user.getRole().getName().equalsIgnoreCase("admin")) {
            redirectAttributes.addFlashAttribute("info", "You are not allowed to do that");
            return "redirect:/admin/users";
        }
        userService.deleteById(id);
        redirectAttributes.addFlashAttribute("info", "User deleted successfully");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/update/{id}")
    public String update(
            @PathVariable Long id,
            @RequestParam("role") String roleStr,
            @Valid @Email @RequestParam("email") String email,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        ApplicationUser user = userService.findByUsername(principal.getName());
        if(!user.getRole().getName().equalsIgnoreCase("admin")) {
            redirectAttributes.addFlashAttribute("info", "You are not allowed to do that");
            return "redirect:/admin/users";
        }

        try{
            Role role = roleService.getRoleByName(roleStr);
            userService.changeUserDataById(id, role, email);
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Role " + roleStr + " or user does not exist");
            return "redirect:/admin/users";
        }
        redirectAttributes.addFlashAttribute("info", "Data saved successfully");
        return "redirect:/admin/users";
    }
}
