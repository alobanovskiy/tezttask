package org.example.test.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public final class HttpApiClient {
    private final HttpClient http;
    private final ObjectMapper mapper;
    private final String baseUrl;

    public HttpApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.mapper = new ObjectMapper();
        this.http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    @Step("HTTP {method} {path}")
    public <T> T postJson(String path, Object body, String bearerToken, int expectedStatus, Class<T> responseType) {
        return exchangeJson("POST", path, body, bearerToken, expectedStatus, responseType, null);
    }

    @Step("HTTP {method} {path}")
    public <T> T postJson(String path, Object body, String bearerToken, int expectedStatus, TypeReference<T> responseType) {
        return exchangeJson("POST", path, body, bearerToken, expectedStatus, null, responseType);
    }

    @Step("HTTP {method} {path}")
    public <T> T getJson(String path, String bearerToken, int expectedStatus, TypeReference<T> responseType) {
        return exchangeJson("GET", path, null, bearerToken, expectedStatus, null, responseType);
    }

    @Step("HTTP {method} {path}")
    public <T> T deleteJson(String path, String bearerToken, int expectedStatus, Class<T> responseType) {
        return exchangeJson("DELETE", path, null, bearerToken, expectedStatus, responseType, null);
    }

    private <T> T exchangeJson(
            String method,
            String path,
            Object body,
            String bearerToken,
            int expectedStatus,
            Class<T> responseClass,
            TypeReference<T> responseTypeRef
    ) {
        try {
            String url = baseUrl + ApiConstants.API_BASE_PATH + path;
            String requestJson = body == null ? null : mapper.writeValueAsString(body);

            HttpRequest.Builder req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    .header("accept", "application/json");

            if (bearerToken != null && !bearerToken.isBlank()) {
                req.header("Authorization", "Bearer " + bearerToken);
            }

            if ("POST".equals(method)) {
                req.header("Content-Type", "application/json");
                req.POST(HttpRequest.BodyPublishers.ofString(requestJson == null ? "{}" : requestJson, StandardCharsets.UTF_8));
            } else if ("GET".equals(method)) {
                req.GET();
            } else if ("DELETE".equals(method)) {
                req.DELETE();
            } else {
                throw new IllegalArgumentException("Unsupported method: " + method);
            }

            attach("Request", method + " " + url + "\n" + (requestJson == null ? "" : requestJson));
            HttpResponse<String> resp = http.send(req.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            attach("Response", "HTTP " + resp.statusCode() + "\n" + resp.body());

            assertThat(resp.statusCode()).isEqualTo(expectedStatus);

            if (responseClass == Void.class || responseTypeRef == null && responseClass == null) {
                return null;
            }
            if (responseTypeRef != null) {
                return mapper.readValue(resp.body(), responseTypeRef);
            }
            return mapper.readValue(resp.body(), responseClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void attach(String name, String content) {
        Allure.addAttachment(name, "text/plain",
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)),
                ".txt");
    }
}

