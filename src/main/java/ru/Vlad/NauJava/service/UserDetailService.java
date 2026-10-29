package ru.Vlad.NauJava.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.Vlad.NauJava.entity.User;
import ru.Vlad.NauJava.repository.UserRepository;

@Component
public class UserDetailService implements UserDetailsService {
    private UserRepository userRepository;

    public UserDetailService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User appUser = userRepository.findByUsername(username).
                orElseThrow(() -> new  UsernameNotFoundException("Пользователь с юзернеймом " + username + " не найден"));;

        org.springframework.security.core.userdetails.User user = new
                org.springframework.security.core.userdetails.User(
                appUser.getUsername(), appUser.getPassword(),
                mapRoles(appUser));
        return user;
    }
    private Collection<GrantedAuthority> mapRoles(User appUser)
    {
        return List.of(new SimpleGrantedAuthority("ROLE_" + appUser.getRole()));
    }
}
