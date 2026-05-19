package ru.pocketbyte.locolaser.config.resources

import java.io.File
import java.io.Serializable

/**
 * Functional interface for resolving the [File] path of a resource
 * for a given locale, directory, name, and extension.
 */
fun interface ResourceFileProvider: Serializable {

    /**
     * Returns the [File] for the resource identified by the given parameters.
     * @param locale The locale string (e.g., `"en"`, `"ru"`).
     * @param directory The base resources directory.
     * @param name The resource name.
     * @param extension The file extension (e.g., `"xml"`, `"strings"`).
     * @return The resolved resource [File].
     */
    fun get(locale: String, directory: File, name: String, extension: String): File
}
