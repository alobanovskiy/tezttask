package org.example.test;

import io.qameta.allure.*;
import org.example.test.api.AuthApi;
import org.example.test.api.PlayersApi;
import org.example.test.support.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.test.support.ApiConstants.EMAIL_DOMAIN;
import static org.example.test.support.Dtos.PlayerRequestDTO;
import static org.example.test.support.Dtos.PlayerResponseDTO;
import static org.example.test.support.PlayerAssertions.assertCreatedMatchesRequest;
import static org.example.test.support.PlayerAssertions.assertMatchesDocs;

@Epic("Automation task for betting")
@Feature("Players API smoke test")
@Owner("Aleksandr Lobanovsky")
@Severity(SeverityLevel.BLOCKER)
@Tag("task")
class PlayersApiTaskTest extends BaseApiTest {

    private static final List<String> CURRENCIES = List.of(
            "USD", "EUR", "GBP", "RUB", "UAH", "KZT",
            "PLN", "TRY", "AZN", "CAD", "AUD", "SEK"
    );

    private String token;
    private PlayersApi playersApi;
    private List<PlayerRequestDTO> createdRequests = List.of();
    private List<PlayerResponseDTO> createdPlayers = List.of();

    @BeforeEach
    void prepareUsers() {
        playersApi = new PlayersApi(api);
        token = new AuthApi(api).loginAndGetToken();

        deleteAllWithTestDomain(playersApi, token);

        createdRequests = IntStream.range(0, CURRENCIES.size())
                .mapToObj(i -> TestData.newPlayer(CURRENCIES.get(i)))
                .collect(Collectors.toCollection(ArrayList::new));

        createdPlayers = createdRequests.stream()
                .map(req -> {
                    PlayerResponseDTO created = playersApi.create(token, req);
                    assertCreatedMatchesRequest(created, req);
                    return created;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @AfterEach
    void cleanupUsers() {
        if (playersApi == null || token == null) {
            return;
        }
        for (PlayerResponseDTO created : createdPlayers) {
            if (created.id() != null && !created.id().isBlank()) {
                playersApi.deleteOne(token, created.id());
            }
        }

        List<PlayerResponseDTO> afterDelete = playersApi.getAll(token);
        Set<String> remainingEmails = afterDelete.stream()
                .map(PlayerResponseDTO::email)
                .collect(Collectors.toSet());

        for (PlayerRequestDTO req : createdRequests) {
            assertThat(remainingEmails)
                    .as("created email should be deleted: " + req.email())
                    .doesNotContain(req.email());
        }
        assertThat(afterDelete)
                .extracting(PlayerResponseDTO::email)
                .noneMatch(e -> e != null && e.endsWith("@" + EMAIL_DOMAIN));
    }

    @Test
    @Story("ome test for players")
    @Description("""
            в тест вынесены шаги
            3) Запросить профиль созданного игрока (/api/automationTask/getOne)
            4) Запросить всех и отсортировать по имени (/api/automationTask/getAll)
            шаги создания вынесены в предусловия
            в постусловия вынесены шаги удаления пользователей
            """)
    void automationTaskScenario() {
        PlayerRequestDTO anyRequest = createdRequests.get(0);
        PlayerResponseDTO profile = playersApi.getOneByEmail(token, anyRequest.email());
        assertMatchesDocs(profile);
        assertThat(profile.email()).isEqualTo(anyRequest.email());

        List<PlayerResponseDTO> all = playersApi.getAll(token);
        assertThat(all).isNotNull();

        Set<String> emailsInAll = all.stream()
                .map(PlayerResponseDTO::email)
                .collect(Collectors.toSet());
        for (PlayerRequestDTO req : createdRequests) {
            assertThat(emailsInAll)
                    .as("getAll contains created email: " + req.email())
                    .contains(req.email());
        }

        List<PlayerResponseDTO> sortedByName = all.stream()
                .sorted(Comparator.comparing(PlayerResponseDTO::name, Comparator.nullsLast(String::compareToIgnoreCase)))
                .toList();

        assertThat(sortedByName)
                .extracting(PlayerResponseDTO::name)
                .isSortedAccordingTo(String.CASE_INSENSITIVE_ORDER);
    }

    private static void deleteAllWithTestDomain(PlayersApi playersApi, String token) {
        List<PlayerResponseDTO> all = playersApi.getAll(token);
        for (PlayerResponseDTO p : all) {
            String email = p.email();
            if (email != null && email.endsWith("@" + EMAIL_DOMAIN) && p.id() != null && !p.id().isBlank()) {
                playersApi.deleteOne(token, p.id());
            }
        }
    }
}
