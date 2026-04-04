package org.example.test.api;

import io.qameta.allure.Step;
import org.example.test.support.TestConfig;
import org.example.test.support.HttpApiClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.test.support.Dtos.CredentialsDTO;
import static org.example.test.support.Dtos.LoginResponseDTO;
import static org.example.test.support.ApiPaths.LOGIN;

public final class AuthApi {
    private final HttpApiClient api;

    public AuthApi(HttpApiClient api) {
        this.api = api;
    }

    @Step("Login and get access token")
    public String loginAndGetToken() {
        LoginResponseDTO resp = api.postJson(
                LOGIN,
                new CredentialsDTO(TestConfig.email(), TestConfig.password()),
                null,
                201,
                LoginResponseDTO.class
        );

        assertThat(resp.accessToken()).isNotBlank();
        return resp.accessToken();
    }
}

