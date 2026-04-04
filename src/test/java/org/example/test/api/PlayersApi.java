package org.example.test.api;

import com.fasterxml.jackson.core.type.TypeReference;
import io.qameta.allure.Step;
import org.example.test.support.HttpApiClient;

import java.util.List;

import static org.example.test.support.Dtos.PlayerRequestDTO;
import static org.example.test.support.Dtos.PlayerRequestOneDTO;
import static org.example.test.support.Dtos.PlayerResponseDTO;
import static org.example.test.support.ApiPaths.PLAYERS_CREATE;
import static org.example.test.support.ApiPaths.PLAYERS_DELETE_ONE_PREFIX;
import static org.example.test.support.ApiPaths.PLAYERS_GET_ALL;
import static org.example.test.support.ApiPaths.PLAYERS_GET_ONE;

public final class PlayersApi {
    private final HttpApiClient api;

    public PlayersApi(HttpApiClient api) {
        this.api = api;
    }

    @Step("Create player")
    public PlayerResponseDTO create(String token, PlayerRequestDTO request) {
        return api.postJson(PLAYERS_CREATE, request, token, 201, PlayerResponseDTO.class);
    }

    @Step("Get player by email")
    public PlayerResponseDTO getOneByEmail(String token, String email) {
        return api.postJson(PLAYERS_GET_ONE, new PlayerRequestOneDTO(email), token, 201, PlayerResponseDTO.class);
    }

    @Step("Get all players")
    public List<PlayerResponseDTO> getAll(String token) {
        return api.getJson(PLAYERS_GET_ALL, token, 200, new TypeReference<>() {
        });
    }

    @Step("Delete player by id")
    public PlayerResponseDTO deleteOne(String token, String id) {
        return api.deleteJson(PLAYERS_DELETE_ONE_PREFIX + id, token, 200, PlayerResponseDTO.class);
    }
}

