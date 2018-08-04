package ru.pocketbyte.locolaser.platform.mobile.utils;

public class ObjectiveCUtils {

    public static String escapeString(String string) {
        return string
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }

    public static String escapeComment(String string) {
        return string
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }

}
