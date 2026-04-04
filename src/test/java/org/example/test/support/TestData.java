package org.example.test.support;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

import static org.example.test.support.Dtos.PlayerRequestDTO;

public final class TestData {
    private TestData() {
    }

    private static final SecureRandom RND = new SecureRandom();

    public static PlayerRequestDTO newPlayer(String currencyCode) {
        String ts = String.valueOf(Instant.now().toEpochMilli());
        String username = "test_user_" + ts;
        String email = username + "@" + ApiConstants.EMAIL_DOMAIN;
        String password = "p4ss" + (1000 + RND.nextInt(9000));

        return new PlayerRequestDTO(
                currencyCode,
                email,
                "Name" + ts,
                password,
                password,
                "Surname" + ts,
                username
        );
    }
}

