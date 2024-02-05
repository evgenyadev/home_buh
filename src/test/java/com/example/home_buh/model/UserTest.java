package com.example.home_buh.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserTest {

    @Test
    public void testUserConstructorAndGetters() {
        // Создаем пользователя
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setUsername("test_user");
        user.setEnabled(true);

        // Проверяем геттеры
        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals("test_user", user.getUsername());
        assertTrue(user.isEnabled());
    }

    @Test
    public void testUserAuthorities() {
        // Создаем пользователя
        User user = new User();

        // Проверяем, что у пользователя нет ролей
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    public void testUserAccountExpiration() {
        // Создаем пользователя
        User user = new User();

        // Проверяем, что счет пользователя не истек
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    public void testUserAccountLock() {
        // Создаем пользователя
        User user = new User();

        // Проверяем, что счет пользователя не заблокирован
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    public void testUserCredentialsExpiration() {
        // Создаем пользователя
        User user = new User();

        // Проверяем, что у пользователя нет срока действия учетных данных
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    public void testUserEnabled() {
        // Создаем пользователя
        User user = new User();

        // Проверяем, что пользователь включен
        assertTrue(user.isEnabled());
    }
}
