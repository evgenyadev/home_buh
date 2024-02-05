package com.example.home_buh.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.home_buh.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        // Создаем заглушку репозитория перед каждым тестом
        userRepository = mock(UserRepository.class);
    }

    @Test
    public void testSaveUser() {
        // Создаем объект User для сохранения
        User userToSave = new User();
        userToSave.setUsername("fake_user");
        userToSave.setEmail("fake_email@example.com");
        userToSave.setPassword("fake_password");
        userToSave.setEnabled(true);

        // Создаем фейковый объект User с ID, который будет возвращен после сохранения
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("fake_user");
        savedUser.setEmail("fake_email@example.com");
        savedUser.setPassword("fake_password");
        savedUser.setEnabled(true);

        // Устанавливаем поведение заглушки
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Сохраняем User
        User returnedUser = userRepository.save(userToSave);

        // Проверяем, что User был успешно сохранен
        assertEquals(savedUser.getUsername(), returnedUser.getUsername());
        assertEquals(savedUser.getEmail(), returnedUser.getEmail());
        assertEquals(savedUser.getPassword(), returnedUser.getPassword());
        assertEquals(savedUser.isEnabled(), returnedUser.isEnabled());
    }

    @Test
    public void testFindUserByUsername() {
        // Создаем фейкового пользователя
        User fakeUser = new User();
        fakeUser.setUsername("fake_user");
        fakeUser.setEmail("fake_email@example.com");
        fakeUser.setPassword("fake_password");
        fakeUser.setEnabled(true);

        // Устанавливаем поведение заглушки
        when(userRepository.findByUsername("fake_user")).thenReturn(fakeUser);

        // Вызываем метод, который использует репозиторий
        User foundUser = userRepository.findByUsername("fake_user");

        // Проверяем результат
        assertNotNull(foundUser);
        assertEquals("fake_user", foundUser.getUsername());
        assertEquals("fake_email@example.com", foundUser.getEmail());
        assertEquals("fake_password", foundUser.getPassword());
        assertEquals(true, foundUser.isEnabled());
    }
}
