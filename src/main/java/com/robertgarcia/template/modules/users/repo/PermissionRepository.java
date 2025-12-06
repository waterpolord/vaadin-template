package com.robertgarcia.template.modules.users.repo;

import com.robertgarcia.template.modules.users.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
}