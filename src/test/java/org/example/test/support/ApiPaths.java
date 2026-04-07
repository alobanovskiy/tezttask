package org.example.test.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiPaths {

    public static final String LOGIN = "/tester/login";

    public static final String PLAYERS_CREATE = "/automationTask/create";
    public static final String PLAYERS_GET_ONE = "/automationTask/getOne";
    public static final String PLAYERS_GET_ALL = "/automationTask/getAll";
    public static final String PLAYERS_DELETE_ONE_PREFIX = "/automationTask/deleteOne/";
}

