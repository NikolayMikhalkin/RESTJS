package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Collection<User> findAll();

    Optional<User> getById(Integer id);

    User findByUsername(String username);

    void save(User user);

    void getDelete(Integer userId);

    void update(User user);
}
