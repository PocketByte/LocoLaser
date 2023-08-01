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
 * Extension of the ResourceFile that allow writing strings into resource file through open OutputStream.
 * Before writing the stream should be open via open(). When writing is completed the stream should be closed via close().
 *
 * @author Denis Shurygin
 */
abstract class ResourceStreamFile(
    /**
     * File of the resource.
     */
    val file: File
) : ResourceFile {


    private var stream: OutputStream? = null

    val isOpen: Boolean
        get() = stream != null

    /**
     * Open stream for writing.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun open() {
        if (stream == null) {
            file.parentFile.mkdirs()
            stream = FileOutputStream(file)
        }
    }

    /**
     * Close stream.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun close() {
        if (stream != null) {
            stream!!.flush()
            stream!!.close()
            stream = null
        }
    }

    /**
     * Write string into stream.
     * @param string String for writing.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun writeString(string: String) {
        if (stream == null)
            throw IllegalStateException("You should open file before write")

        stream!!.write(string.toByteArray(charset("UTF-8")))
    }

    @Throws(IOException::class)
    fun writeStringLn(string: String) {
        writeString(string)
        writeln()
    }

    /**
     * Write new line symbol into stream.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun writeln() {
        if (stream == null)
            throw IllegalStateException("You should open file before write")

        stream!!.write(0x0D)
        stream!!.write(0x0A)
    }

}
