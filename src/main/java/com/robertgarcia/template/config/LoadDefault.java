package com.robertgarcia.template.config;

import com.robertgarcia.template.modules.users.domain.Permission;
import com.robertgarcia.template.modules.users.domain.Role;
import com.robertgarcia.template.modules.users.domain.User;
import com.robertgarcia.template.modules.users.repo.PermissionRepository;
import com.robertgarcia.template.modules.users.repo.RoleRepository;
import com.robertgarcia.template.modules.users.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class LoadDefault {
    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository,
                                               PermissionRepository permissionRepository,
                                               RoleRepository roleAppRepository) {
        return args -> {
            if(userRepository.findAll().isEmpty()){
                List<Role> roles = List.of(
                        new Role("ADMIN", "Acceso total al sistema"),
                        new Role("Digitador", "Encargado de ingresar productos"),
                        new Role("Contador", "Contabiliza los productos y escanea")
                );
                roles = roleAppRepository.saveAll(roles);

                List<Permission> defaultPermissions = List.of(
                        new Permission("READ_USERS", "Acceder a lista de usuarios","USERS"),
                        new Permission("WRITE_USERS", "Permite crear y actualizar usuarios","USERS"),
                        new Permission("DELETE_USERS", "Permite eliminar usuarios","USERS"),

                        new Permission("READ_PRODUCTS", "Permite leer información de productos", "PRODUCTS"),
                        new Permission("WRITE_PRODUCTS", "Permite crear y actualizar productos","PRODUCTS"),

                        new Permission("CASH_ACCOUNTING", "Permite crear y actualizar cuadres","CASH_ACCOUNTING"),

                        new Permission("READ_ROUTES", "Permite ver información de rutas", "ROUTES"),
                        new Permission("WRITE_ROUTES", "Permite crear y actualizar rutas","ROUTES"),
                        new Permission("DELETE_ROUTES", "Permite eliminar rutas","ROUTES"),

                        new Permission("READ_BUSINESS", "Permite ver información de clientes", "BUSINESS"),
                        new Permission("WRITE_BUSINESS", "Permite crear y actualizar clientes","BUSINESS"),
                        new Permission("DELETE_BUSINESS", "Permite eliminar clientes","BUSINESS"),

                        /*new Permission("READ_EXPENSES", "Permite ver información de gastos"),
                        new Permission("WRITE_EXPENSES", "Permite crear y actualizar gastos"),
                        new Permission("DELETE_EXPENSES", "Permite eliminar gastos"),

                        new Permission("WRITE_PLANS", "Permite editar y cambiar plan de pago"),

                        new Permission("WRITE_PRODUCT_HISTORY", "Editar historial de productos"),
                        new Permission("READ_PRODUCT_HISTORY", "Visualizar historial de productos"),

                        */

                        new Permission("WRITE_SETTINGS", "Permite actualizar la configuración","SETTINGS"),

                        new Permission("READ_DASHBOARD", "Permite ver tablero","DASHBOARD")

                );
                permissionRepository.saveAll(defaultPermissions);


                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("123456"));
                adminUser.setFirstName("Super");
                adminUser.setLastName("Admin");
                adminUser.setEmail("admin@example.com");
                adminUser.setPhone("1234567890");
                adminUser.setIdentification("000-0000000-0");
                adminUser.setRole(roles.get(0));
                adminUser.setPermissions(new ArrayList<>());
                adminUser.setDeleted(false);
                adminUser.setOwner(true);
                userRepository.save(adminUser);

            }
        };
    }
}
