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
 * Utils class that calculate hash sum of the file.
 *
 * @author Denis Shurygin
 */
object HashUtils {

    /**
     * Gets MD5 Checksum of the file.
     * @param file File.
     * @return MD5 Checksum of the file.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun getMD5Checksum(file: File?): String? {
        if (file == null || !file.exists())
            return null

        val checksum = createChecksum(file)
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
    private fun createChecksum(file: File): ByteArray? {
        val fileInputStream = FileInputStream(file)

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
}
