package ru.kata.spring.boot_security.demo.controllers;

import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.Collection;
import java.util.Optional;


@RestController
@RequestMapping({"/api/admin"})
public class AdminControllerRest {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminControllerRest(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public ResponseEntity<Collection<User>> getAllUsers() {
        Collection<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable("id") Integer id) {
        Optional<User> foundUser = userService.getById(id);

        return new ResponseEntity<>(foundUser, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<Collection<Role>> getRoles() {
        Collection<Role> roles = roleService.getAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    // @RequestBody - принимает JSON от клиента и конвертирует его в Java объекты
    @PostMapping("/new")
    public ResponseEntity<User> create(@RequestBody User user) {
        this.userService.save(user);

        return ResponseEntity.ok().body(user);
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody User user,
                                             @PathVariable("id") Integer id) {
        this.userService.update(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Integer id) {
        this.userService.getDelete(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}