/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.utils

/**
 * @author Denis Shurygin
 */
object LogUtils {

    fun err(message: String) {
        System.err.println("ERROR: $message")
    }

    fun err(exception: Exception) {
        System.err.println("ERROR: ")
        exception.printStackTrace()
    }

    fun warn(message: String) {
        println("WARNING: $message")
    }

    fun info(message: String) {
        println(message)
    }
}
