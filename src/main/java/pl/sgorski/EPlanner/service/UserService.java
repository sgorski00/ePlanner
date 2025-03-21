package pl.sgorski.EPlanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.sgorski.EPlanner.dto.RegisterRequest;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.model.Role;
import pl.sgorski.EPlanner.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public ApplicationUser save(ApplicationUser user) {
        return userRepository.save(user);
    }

    public ApplicationUser findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User " + username + " not found")
        );
    }

    public void delete(ApplicationUser user) {
        userRepository.delete(user);
    }

    public List<ApplicationUser> getAllUsers() {
        return userRepository.findAll();
    }

    public List<ApplicationUser> getUsersWithRole(Role role) {
        return userRepository.findAllByRole(role);
    }

    public ApplicationUser register(RegisterRequest registerRequest) {
        Role userRole = roleService.getRoleByName("USER");
        ApplicationUser user = new ApplicationUser();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setRole(userRole);
        return save(user);
    }
}
