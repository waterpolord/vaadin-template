package com.robertgarcia.template.modules.users.repo;

import com.robertgarcia.template.modules.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAllByDeletedIsFalse();
    User findByUsername(String s);
}