package pl.sgorski.EPlanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sgorski.EPlanner.model.Role;
import pl.sgorski.EPlanner.repository.RoleRepository;

import java.util.List;

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

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
