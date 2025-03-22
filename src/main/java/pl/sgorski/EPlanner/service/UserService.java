package pl.sgorski.EPlanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.model.Role;
import pl.sgorski.EPlanner.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    public void changeUserDataById(long id, Role role, String email) {
        ApplicationUser user = userRepository.findById(id).orElseThrow();
        user.setRole(role);
        user.setEmail(email);
        userRepository.save(user);
    }
}
