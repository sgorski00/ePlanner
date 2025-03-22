package pl.sgorski.EPlanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sgorski.EPlanner.model.ApplicationUser;
import pl.sgorski.EPlanner.model.Role;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByUsername(String username);
    List<ApplicationUser> findAllByRole(Role role);
}
