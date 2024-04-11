package ru.kata.spring.boot_security.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.services.RoleServiceImp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class Init implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleServiceImp roleServiceImp;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public Init(RoleServiceImp roleService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.roleServiceImp = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Role userRole = new Role();
        userRole.setRole("ROLE_USER");
        roleServiceImp.save(userRole);

        Role adminRole = new Role();
        adminRole.setRole("ROLE_ADMIN");
        roleServiceImp.save(adminRole);

        Set<Role> userRoles = new HashSet<>(Arrays.asList(userRole, adminRole));
        Set<Role> adminRoles = new HashSet<>(Arrays.asList(adminRole));

        User admin = new User ();
        admin.setEmail("admin@mail.ru");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setAge((byte) 30);
        admin.setRoles(adminRoles);

        User user = new User();
        user.setEmail("user@mail.ru");
        user.setPassword(passwordEncoder.encode("user"));
        user.setFirstName("user");
        user.setLastName("user");
        user.setAge((byte) 30);
        user.setRoles(userRoles);

        userRepository.save(admin);
        userRepository.save(user);
    }
}
