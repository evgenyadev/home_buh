package com.example.home_buh.service;

import com.example.home_buh.model.Authority;
import com.example.home_buh.model.User;
import com.example.home_buh.repository.UserRepository;
import com.example.home_buh.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        Authority authority = new Authority();
        authority.setUsername(user.getUsername());
        authority.setAuthority("ROLE_USER"); // new user take role USER
        authorityRepository.save(authority);
        System.out.println("Сохраняем пользователя: " + user);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // Пользователь не аутентифицирован
            return null;
        }

        String username = authentication.getName();
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));

        // Пользователь не найден в базе данных
        return optionalUser.orElse(null);
    }
}
