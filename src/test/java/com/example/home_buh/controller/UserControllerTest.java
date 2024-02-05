package com.example.home_buh.controller;

import com.example.home_buh.model.User;
import com.example.home_buh.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализация моков и инъекция зависимостей
    }

    @Test
    public void testRegistrationForm() {
        String viewName = userController.registrationForm(model);
        assertEquals("register", viewName);
        verify(model).addAttribute("user", new User());
    }

    @Test
    public void testRegistrationSubmit() {
        User user = new User();
        String redirectURL = userController.registrationSubmit(user);
        assertEquals("redirect:/login", redirectURL);
        verify(userService, times(1)).save(user);
    }
}
