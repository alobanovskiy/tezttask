package org.example.test;

import org.example.test.support.HttpApiClient;
import org.junit.jupiter.api.BeforeAll;

import static org.example.test.support.TestConfig.baseUrl;

public abstract class BaseApiTest {
    protected static HttpApiClient api;

    @BeforeAll
    static void setUpClient() {
        api = new HttpApiClient(baseUrl());
    }
}
