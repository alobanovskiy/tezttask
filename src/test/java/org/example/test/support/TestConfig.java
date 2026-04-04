package org.example.test.support;

public final class TestConfig {
    private TestConfig() {
    }

    public static String baseUrl() {
        return get("BASE_URL", ApiConstants.HOST);
    }

    public static String email() {
        return firstNonBlank(get("EMAIL", null), LoginConfig.EMAIL);
    }

    public static String password() {
        return firstNonBlank(get("PASSWORD", null), LoginConfig.PASSWORD);
    }

    private static String get(String key, String defaultValue) {
        String fromProp = System.getProperty(key);
        if (fromProp != null && !fromProp.isBlank()) return fromProp.trim();
        String fromEnv = System.getenv(key);
        if (fromEnv != null && !fromEnv.isBlank()) return fromEnv.trim();
        return defaultValue;
    }

    private static String firstNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) return a;
        if (b != null && !b.isBlank()) return b;
        throw new IllegalStateException("Missing default value, ro value from config)");
    }
}

