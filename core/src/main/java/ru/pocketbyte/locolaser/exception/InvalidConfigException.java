/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.exception;

/**
 * @author Denis Shurygin
 */
public class InvalidConfigException extends Exception {
    private static final String DEFAULT_MESSAGE = "Invalid Config!";

    public InvalidConfigException() {
        this(null);
    }

    public InvalidConfigException(String message) {
        super(message != null ? DEFAULT_MESSAGE + " " + message : DEFAULT_MESSAGE);
    }
}
