/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.utils;

/**
 * @author Denis Shurygin
 */
public class TextUtils {

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0 || string.trim().length() == 0;
    }
}
