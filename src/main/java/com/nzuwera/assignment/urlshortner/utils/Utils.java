package com.nzuwera.assignment.urlshortner.utils;

import java.util.UUID;

public class Utils {
    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    public static String generateUrlId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0,8);
    }
}
