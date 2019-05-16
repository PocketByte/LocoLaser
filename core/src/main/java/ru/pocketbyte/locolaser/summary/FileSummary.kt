/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.summary

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.utils.HashUtils

import java.io.File
import java.io.IOException

/**
 * Summary for single file.
 *
 * @author Denis Shurygin
 */
class FileSummary {

    companion object {
        internal val BYTES = "BYTES"
        internal val HASH = "HASH"
    }

    val bytes: Long
    val hash: String?

    constructor(bytesCount: Long, hash: String?) {
        this.bytes = bytesCount
        this.hash = hash
    }

    constructor(json: JSONObject) {
        bytes = json[BYTES] as? Long ?: 0L
        hash = json[HASH] as? String
    }

    constructor(file: File?) {
        if (file != null) {
            bytes = file.length()
            hash = getHashFromFile(file)
        } else {
            bytes = 0
            hash = null
        }
    }

    constructor(files: Array<File?>?) {
        var bytes: Long = 0
        val hash = StringBuilder()
        files?.forEach { file ->
            bytes += file?.length() ?: 0
            if (file != null) {
                try {
                    val fileHash = HashUtils.getMD5Checksum(file)
                    hash.append(fileHash)
                } catch (e: IOException) {
                    // failed to get hash
                    hash.append("null")
                }
            }
        }
        this.bytes = bytes
        this.hash = hash.toString()
    }

    fun toJson(): JSONObject {
        return JSONObject().apply {
            put(BYTES, bytes)
            put(HASH, hash)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true

        if (other == null)
            return false

        if (javaClass != other.javaClass)
            return false

        val summary = other as FileSummary?
        return bytes != 0L
                && bytes == summary?.bytes
                && hash != null
                && hash == summary.hash
    }

    /**
     * Check if summary of the specified file is equal this summary.
     * @param file File for check.
     * @return `true` if summary of the argument file equal this summary; `false` otherwise.
     */
    fun equalsToFile(file: File?): Boolean {
        if (file == null)
            return false

        return bytes != 0L
                && bytes == file.length()
                && hash != null
                && hash == getHashFromFile(file)
    }

    private fun getHashFromFile(file: File): String? {
        try {
            return HashUtils.getMD5Checksum(file)
        } catch (e: IOException) {
            return null
        }

    }

    override fun toString(): String {
        return toJson().toJSONString()
    }
}
