package com.fabbe50.fabsservertweaks.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringUtil {
    public static String capitalizeFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }
}
