package pl.sgorski.EPlanner.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.model.Role;
import pl.sgorski.EPlanner.service.RoleService;
import pl.sgorski.EPlanner.service.UserService;

import java.util.List;

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
}
