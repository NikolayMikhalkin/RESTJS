package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import ru.kata.spring.boot_security.demo.models.User;

import java.security.Principal;


@Controller
@RequestMapping
public class AdminController {

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }
}