package ru.pocketbyte.locolaser.platform.mobile.utils;

public class SwiftUtils {

    public static String escapeString(String string) {
        return string
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\u0009", "\\t");
    }

    public static String escapeComment(String string) {
        return string
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\u0009", "  ");
    }
}
