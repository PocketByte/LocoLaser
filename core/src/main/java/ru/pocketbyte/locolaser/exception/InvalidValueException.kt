package ru.pocketbyte.locolaser.exception

/**
 * Exception thrown when an invalid value is encountered during resource processing.
 *
 * @param message Optional detail message describing the invalid value.
 */
class InvalidValueException @JvmOverloads constructor(
        message: String? = null
) : Exception(message)
