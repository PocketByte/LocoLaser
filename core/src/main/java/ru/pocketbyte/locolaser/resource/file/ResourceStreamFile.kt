/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.file

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * A [ResourceFile] that writes content to an [OutputStream].
 * The stream must be opened via [open] before writing and closed via [close] when done.
 *
 * @author Denis Shurygin
 */
abstract class ResourceStreamFile(
    /**
     * The resource file this stream writes to.
     */
    val file: File
) : ResourceFile {


    private var stream: OutputStream? = null

    /**
     * True if the output stream is currently open.
     */
    val isOpen: Boolean
        get() = stream != null

    /**
     * Opens the output stream for writing.
     * @throws IOException if an I/O error occurs while opening the file.
     */
    @Throws(IOException::class)
    fun open() {
        if (stream == null) {
            file.parentFile.mkdirs()
            stream = FileOutputStream(file)
        }
    }

    /**
     * Flushes and closes the output stream.
     * @throws IOException if an I/O error occurs while closing the stream.
     */
    @Throws(IOException::class)
    fun close() {
        stream?.apply {
            flush()
            close()
        }
        stream = null
    }

    /**
     * Writes [string] to the stream encoded as UTF-8.
     * @param string The string to write.
     * @throws IOException if an I/O error occurs or the stream is not open.
     */
    @Throws(IOException::class)
    fun writeString(string: String) {
        if (stream == null)
            throw IllegalStateException("You should open file before write")

        stream?.write(string.toByteArray(charset("UTF-8")))
    }

    /**
     * Writes [string] followed by a newline to the stream.
     * @throws IOException if an I/O error occurs or the stream is not open.
     */
    @Throws(IOException::class)
    fun writeStringLn(string: String) {
        writeString(string)
        writeln()
    }

    /**
     * Writes a newline (CR+LF) to the stream.
     * @throws IOException if an I/O error occurs or the stream is not open.
     */
    @Throws(IOException::class)
    fun writeln() {
        if (stream == null)
            throw IllegalStateException("You should open file before write")

        stream?.write(0x0D)
        stream?.write(0x0A)
    }
}
