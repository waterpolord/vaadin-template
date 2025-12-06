package com.robertgarcia.template.modules.users.service;


import com.robertgarcia.template.modules.users.domain.Permission;
import com.robertgarcia.template.modules.users.domain.User;
import com.robertgarcia.template.modules.users.repo.UserRepository;
import com.robertgarcia.template.shared.crud.CrudService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class UserService implements CrudService<User,Integer>, UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> findAll() {
        return userRepository.findAllByDeletedIsFalse();
    }


    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }


    public User save(User entity) {
        entity.setDeleted(false);
        return userRepository.save(entity);
    }


    public void delete(User entity) {
        entity.setDeleted(true);
        userRepository.save(entity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("Error the user: "+username+" do not exist.");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        String roleName = user.getRole().getName(); // p.ej. "ADMIN"
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }
        authorities.add(new SimpleGrantedAuthority(roleName));
        for (Permission permission : user.getPermissions()) {
            String permissionAssignedPage = permission.getAssignedPage();
            String permissionName = permission.getPermission();
            if (!permissionAssignedPage.startsWith("ROLE_")) {
                permissionAssignedPage = "ROLE_" + permissionAssignedPage;
            }
            if (!permissionName.startsWith("ROLE_")) {
                permissionName = "ROLE_" + permissionName;
            }
            authorities.add(new SimpleGrantedAuthority(permissionAssignedPage));
            authorities.add(new SimpleGrantedAuthority(permissionName));
        }

        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()) );
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),true,true,true,
                true,authorities);
    }
}

