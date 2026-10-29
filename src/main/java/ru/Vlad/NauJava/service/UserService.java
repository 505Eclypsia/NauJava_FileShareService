package ru.Vlad.NauJava.service;

import ru.Vlad.NauJava.entity.User;

public interface UserService {
    void addUser (User user);
    User findByUsername(String username);
}
