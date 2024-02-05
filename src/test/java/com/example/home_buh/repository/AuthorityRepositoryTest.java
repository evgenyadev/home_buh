package com.example.home_buh.repository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.home_buh.model.Authority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthorityRepositoryTest {

    private AuthorityRepository authorityRepository;

    @BeforeEach
    public void setUp() {
        // Создаем заглушку репозитория перед каждым тестом
        authorityRepository = mock(AuthorityRepository.class);
    }

    @Test
    public void testSaveAuthority() {
        // Создаем объект Authority для сохранения
        Authority authorityToSave = new Authority();
        authorityToSave.setUsername("fake_user");
        authorityToSave.setAuthority("ROLE_USER");

        // Создаем фейковый объект Authority с ID, который будет возвращен после сохранения
        Authority savedAuthority = new Authority();
        savedAuthority.setUsername("fake_user");
        savedAuthority.setAuthority("ROLE_USER");

        // Устанавливаем поведение заглушки
        when(authorityRepository.save(any(Authority.class))).thenReturn(savedAuthority);

        // Сохраняем Authority
        Authority returnedAuthority = authorityRepository.save(authorityToSave);

        // Проверяем, что Authority был успешно сохранен
        assertEquals(savedAuthority.getUsername(), returnedAuthority.getUsername());
        assertEquals(savedAuthority.getAuthority(), returnedAuthority.getAuthority());
    }

    @Test
    public void testFindAuthorityByUsername() {
        // Создаем фейкового пользователя
        Authority fakeAuthority = new Authority();
        fakeAuthority.setUsername("fake_user");
        fakeAuthority.setAuthority("ROLE_USER");

        // Устанавливаем поведение заглушки
        when(authorityRepository.findByUsername("fake_user")).thenReturn(fakeAuthority);

        // Вызываем метод, который использует репозиторий
        Authority foundAuthority = authorityRepository.findByUsername("fake_user");

        // Проверяем результат
        assertNotNull(foundAuthority);
        assertEquals("fake_user", foundAuthority.getUsername());
        assertEquals("ROLE_USER", foundAuthority.getAuthority());
    }
}
