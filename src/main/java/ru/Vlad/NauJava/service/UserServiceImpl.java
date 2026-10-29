package ru.Vlad.NauJava.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.Vlad.NauJava.entity.Role;
import ru.Vlad.NauJava.entity.User;
import ru.Vlad.NauJava.repository.UserRepository;

import java.time.Instant;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void addUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(Role.USER);
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @PostConstruct
    public void initAdmin() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Role.ADMIN);
            admin.setCreatedAt(Instant.now());
            userRepository.save(admin);
            System.out.println("Создан начальный пользователь: admin / admin (роль ADMIN)");
        }
    }
}
