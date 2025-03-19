package pl.sgorski.EPlanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sgorski.EPlanner.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
