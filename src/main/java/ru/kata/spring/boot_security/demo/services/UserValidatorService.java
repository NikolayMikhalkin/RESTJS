package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;


@Service
public class UserValidatorService {
    private final UserRepository userRepository;

    @Autowired
    public UserValidatorService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User checkByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
