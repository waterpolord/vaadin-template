package com.robertgarcia.template.modules.users.repo;

import com.robertgarcia.template.modules.users.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}