package com.example.home_buh.service;

import com.example.home_buh.model.User;

public interface UserService {
    void save(User user);
    User getCurrentUser();
}