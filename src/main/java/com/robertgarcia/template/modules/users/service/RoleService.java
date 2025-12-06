package com.robertgarcia.template.modules.users.service;

import com.robertgarcia.template.modules.users.domain.Role;
import com.robertgarcia.template.modules.users.repo.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
