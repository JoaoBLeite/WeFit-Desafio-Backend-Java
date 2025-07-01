package br.com.wefit.challenge.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordUtil {

    private final PasswordEncoder passwordEncoder;

    /**
     * Encodes a raw password using the configured {@link PasswordEncoder}.
     *
     * @param rawPassword the raw password to encode
     * @return the encoded password string
     */
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
