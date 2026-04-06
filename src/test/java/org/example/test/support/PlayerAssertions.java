package org.example.test.support;

import io.qameta.allure.Step;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.test.support.Dtos.PlayerRequestDTO;
import static org.example.test.support.Dtos.PlayerResponseDTO;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerAssertions {

    @Step("Assert player response matches documentation")
    public static void assertMatchesDocs(PlayerResponseDTO response) {
        assertThat(response).isNotNull();
        assertThat(response.id()).as("id / _id").isNotBlank();
        assertThat(response.username()).as("username").isNotBlank();
        assertThat(response.email()).as("email").isNotBlank();
        assertThat(response.name()).as("name").isNotBlank();
        assertThat(response.surname()).as("surname").isNotBlank();
    }

    @Step("Assert created player matches request")
    public static void assertCreatedMatchesRequest(PlayerResponseDTO response, PlayerRequestDTO request) {
        assertMatchesDocs(response);
        assertThat(response.email()).isEqualTo(request.email());
        assertThat(response.username()).isEqualTo(request.username());
        assertThat(response.name()).isEqualTo(request.name());
        assertThat(response.surname()).isEqualTo(request.surname());
        if (response.currencyCode() != null) {
            assertThat(response.currencyCode()).isEqualTo(request.currencyCode());
        }
    }
}

