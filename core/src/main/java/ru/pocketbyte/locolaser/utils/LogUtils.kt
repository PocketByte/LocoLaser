/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.utils

/**
 * Utility object for logging errors, warnings, and informational messages.
 *
 * @author Denis Shurygin
 */
object LogUtils {

    /** Prints [message] to stderr prefixed with `"ERROR: "`. */
    fun err(message: String) {
        System.err.println("ERROR: $message")
    }

    /** Prints [exception] to stderr with its stack trace. */
    fun err(exception: Exception) {
        System.err.println("ERROR: ")
        exception.printStackTrace()
    }

    /** Prints [message] to stdout prefixed with `"WARNING: "`. */
    fun warn(message: String) {
        println("WARNING: $message")
    }

    /** Prints [message] to stdout. */
    fun info(message: String) {
        println(message)
    }
}
