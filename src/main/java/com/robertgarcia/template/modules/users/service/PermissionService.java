package com.robertgarcia.template.modules.users.service;

import com.robertgarcia.template.modules.users.domain.Permission;
import com.robertgarcia.template.modules.users.repo.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

}
