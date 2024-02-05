package com.example.home_buh.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthorityTest {

    @Test
    public void testAuthorityModel() {
        // Создание объекта Authority
        Authority authority = new Authority();
        authority.setUsername("user");
        authority.setAuthority("ROLE_USER");

        // Проверка геттеров
        assertEquals("user", authority.getUsername());
        assertEquals("ROLE_USER", authority.getAuthority());

        // Изменение значений
        authority.setUsername("admin");
        authority.setAuthority("ROLE_ADMIN");

        // Проверка измененных значений
        assertEquals("admin", authority.getUsername());
        assertEquals("ROLE_ADMIN", authority.getAuthority());
    }
}
