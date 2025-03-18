package pl.sgorski.EPlanner.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.repository.UserRepository;

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
}
