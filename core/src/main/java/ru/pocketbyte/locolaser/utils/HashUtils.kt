/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.utils

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Gets MD5 Checksum of the file.
 * @param file File.
 * @return MD5 Checksum of the file.
 * @throws IOException
 */
fun File?.getMD5Checksum(): String? {
    if (this == null || !this.exists())
        return null

    val checksum = this.createChecksum()
    if (checksum != null) {
        val resultBuilder = StringBuilder()

        for (aChecksum in checksum) {
            resultBuilder.append(Integer
                .toString((aChecksum.toInt() and 0xff) + 0x100, 16)
                .substring(1))
        }
        return resultBuilder.toString()
    }
    return null
}

@Throws(IOException::class)
private fun File.createChecksum(): ByteArray? {
    val fileInputStream = FileInputStream(this)

    val buffer = ByteArray(1024)
    val complete: MessageDigest
    try {
        complete = MessageDigest.getInstance("MD5")
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        return null
    }

    var numRead: Int

    do {
        numRead = fileInputStream.read(buffer)
        if (numRead > 0) {
            complete.update(buffer, 0, numRead)
        }
    } while (numRead != -1)

    fileInputStream.close()
    return complete.digest()
}