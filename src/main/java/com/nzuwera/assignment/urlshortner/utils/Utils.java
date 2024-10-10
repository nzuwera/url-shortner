package com.nzuwera.assignment.urlshortner.utils;

import java.util.UUID;

public class Utils {

    public static String generateUrlId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
