package ru.Vlad.NauJava.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.Vlad.NauJava.entity.Role;
import ru.Vlad.NauJava.entity.User;
import ru.Vlad.NauJava.repository.UserRepository;

public class UserServiceTest {
    @Test
    void userTest() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserService userService = new UserServiceImpl(mockUserRepository, encoder);
        userService.addUser("TestUser", "qwerty");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(mockUserRepository).save(userCaptor.capture());

        User user = userCaptor.getValue();

        Assertions.assertTrue(encoder.matches("qwerty", user.getPassword()));
        Assertions.assertEquals(Role.USER, user.getRole());
    }
}
