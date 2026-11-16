package ru.Vlad.NauJava.service;

import ru.Vlad.NauJava.entity.User;

public interface UserService {
    void addUser (String username, String password);
    User findByUsername(String username);
}
