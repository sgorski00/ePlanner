package pl.sgorski.EPlanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sgorski.EPlanner.model.Role;
import pl.sgorski.EPlanner.repository.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void saveAllRoles(Iterable<Role> roles) {
        roleRepository.saveAll(roles);
    }
}
