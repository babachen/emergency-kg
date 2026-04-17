package com.bysj.emergencykg.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class PasswordUtils {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public String encode(String password) { return encoder.encode(password); }
    public boolean matches(String rawPassword, String encodedPassword) {
        if (!StringUtils.hasText(encodedPassword)) { return false; }
        if (encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2b$") || encodedPassword.startsWith("$2y$")) { return encoder.matches(rawPassword, encodedPassword); }
        return rawPassword.equals(encodedPassword);
    }
}
