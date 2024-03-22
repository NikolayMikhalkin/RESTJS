package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleServiceImp;
import ru.kata.spring.boot_security.demo.services.UserServiceImp;

import java.security.Principal;

@Controller
@RequestMapping
public class AdminController {

    private final UserServiceImp userService;
    private final RoleServiceImp roleService;

    @Autowired
    public AdminController(UserServiceImp us, RoleServiceImp rs) {
        this.userService = us;
        this.roleService = rs;
    }

    @GetMapping("/admin")
    public String getAdminPage() {
        return "admin";
    }

    @GetMapping("/admin/users")
    public String getAllUsers(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @GetMapping("/admin/update-user")
    public String getUpdateUserPage(Model model, @ModelAttribute("userUpd") User user) {
        model.addAttribute("userUpd", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "update-user";
    }

    @PostMapping("/admin/update-user")
    public String updateUser(@ModelAttribute("userUpd") User user) {
        userService.update(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/create-user")
    public String getCreateUserPage(Model model) {
        model.addAttribute("userCr", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "create-user";
    }

    @PostMapping("/admin/create-user")
    public String createUser(@ModelAttribute("userCr") User user) {
        userService.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/remove-user")
    public String deleteUser(@RequestParam("id") Integer id) {
        userService.getDelete(id);
        return "redirect:/admin/users";
    }

}
