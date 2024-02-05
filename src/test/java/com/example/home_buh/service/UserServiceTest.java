package com.example.home_buh.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.home_buh.model.User;
import com.example.home_buh.service.UserService;
import org.junit.jupiter.api.Test;

public class UserServiceTest {

    @Test
    public void testSaveUser() {
        // Создание мок объекта UserService
        UserService userService = mock(UserService.class);

        // Создание пользователя для тестирования
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");

        // Вызов метода save
        userService.save(user);

        // Проверка, что метод save был вызван один раз
        verify(userService, times(1)).save(user);
    }

    @Test
    public void testGetCurrentUser() {
        // Создание мок объекта UserService
        UserService userService = mock(UserService.class);

        // Создание тестового пользователя
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("testPassword");

        // Установка поведения для метода getCurrentUser
        when(userService.getCurrentUser()).thenReturn(testUser);

        // Вызов метода getCurrentUser
        User currentUser = userService.getCurrentUser();

        // Проверка, что возвращенный пользователь не равен null
        assertNotNull(currentUser);

        // Проверка, что возвращенный пользователь соответствует тестовому пользователю
        assertEquals("testUser", currentUser.getUsername());
        assertEquals("testPassword", currentUser.getPassword());
    }
}
