package org.example.test.support;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Dtos {
    private Dtos() {
    }

    public record CredentialsDTO(
            @JsonProperty("email") String email,
            @JsonProperty("password") String password
    ) {
    }

    public record LoginResponseDTO(
            @JsonProperty("user") UserDTO user,
            @JsonProperty("accessToken") String accessToken
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record UserDTO(
            @JsonProperty("id") String id,
            @JsonProperty("email") String email,
            @JsonProperty("name") String name,
            @JsonProperty("surname") String surname,
            @JsonProperty("role") String role,
            @JsonProperty("position") String position,
            @JsonProperty("status") String status
    ) {
    }

    public record PlayerRequestDTO(
            @JsonProperty("currency_code") String currencyCode,
            @JsonProperty("email") String email,
            @JsonProperty("name") String name,
            @JsonProperty("password_change") String passwordChange,
            @JsonProperty("password_repeat") String passwordRepeat,
            @JsonProperty("surname") String surname,
            @JsonProperty("username") String username
    ) {
    }

    public record PlayerRequestOneDTO(
            @JsonProperty("email") String email
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PlayerResponseDTO(
            @JsonProperty("id")
            @JsonAlias("_id")
            String id,
            @JsonProperty("currency_code") String currencyCode,
            @JsonProperty("email") String email,
            @JsonProperty("name") String name,
            @JsonProperty("surname") String surname,
            @JsonProperty("username") String username
    ) {
    }
}

