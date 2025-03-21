package pl.sgorski.EPlanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sgorski.EPlanner.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNameIgnoreCase(String name);
}
