/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.exception

/**
 * Exception thrown when a localization config is invalid or cannot be processed.
 *
 * @param message Optional detail message appended to the default error message.
 * @author Denis Shurygin
 */
class InvalidConfigException @JvmOverloads constructor(
        message: String? = null
) : Exception(if (message != null) "$DEFAULT_MESSAGE $message" else DEFAULT_MESSAGE) {
    companion object {
        private const val DEFAULT_MESSAGE = "Invalid Config!"
    }
}
