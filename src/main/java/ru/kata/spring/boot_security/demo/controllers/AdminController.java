package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.services.RoleServiceImp;
import ru.kata.spring.boot_security.demo.services.UserServiceImp;

import java.security.Principal;

@Controller
@RequestMapping
public class AdminController {

    private final UserServiceImp userService;
    private final RoleServiceImp roleService;
    private final UserRepository userRepository;

    @Autowired
    public AdminController(UserServiceImp us, RoleServiceImp rs, UserRepository  ur) {
        this.userService = us;
        this.roleService = rs;
        this.userRepository = ur;
    }

    @GetMapping("/admin")
    public String getAdminPage(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("rolesList", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        return "adminpage";
    }

    @GetMapping("/user")
    public String getUserPageInfo(Model model, Principal principal) {
        model.addAttribute("user", userRepository.findUserByUsername(principal.getName()));
        return "user";
    }

    @GetMapping("/new")
    public String getCreateUserPage(Model model, Model role) {
        role.addAttribute("rolesList", roleService.getAllRoles());
        model.addAttribute("user", new User());
        return "new";
    }

    @PostMapping("/admin/new")
    public String add(@ModelAttribute("newUser") User user){
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/update-user")
    public String getUpdateUserPage(Model model, @ModelAttribute("user") User user) {
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "edit";
    }

    @PostMapping("/admin/update-user")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.update(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/remove-user")
    public String deleteUser(@RequestParam Integer id) {
        userService.getDelete(id);
        return "redirect:/admin";
    }
}
