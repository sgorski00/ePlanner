package pl.sgorski.EPlanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, IllegalArgumentException {
        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User " + username + " not found")
        );

        if(user.getProviderId() != null) {
            throw new IllegalArgumentException("User " + username + " is not a local account");
        }

        return (UserDetails) user;
    }
}
