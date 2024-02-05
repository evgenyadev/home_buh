package com.example.home_buh.config;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordEncoderConfigTest {

    @Test
    public void testPasswordEncryption() {
        // Создаем объект BCryptPasswordEncoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Исходный пароль пользователя
        String rawPassword = "password123";

        // Шифруем пароль
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Проверяем, что пароль зашифрован
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }
}
