/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.utils;

/**
 * @author Denis Shurygin
 */
public class LogUtils {

    public static void err(String message) {
        System.err.println("ERROR: " + message);
    }

    public static void err(Exception exception) {
        System.err.println("ERROR: ");
        exception.printStackTrace();
    }

    public static void warn(String message) {
        System.out.println("WARNING: " + message);
    }

    public static void info(String message) {
        System.out.println(message);
    }
}
