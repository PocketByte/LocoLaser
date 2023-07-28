/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.mobile.resource.file

import java.io.File
import java.io.IOException

/**
 * @author Denis Shurygin
 */
class IosResourceFile(file: File, locale: String) : AbsIosStringsResourceFile(file, locale) {

    override val keyValueLinePattern = "\"((?:[^\"]|\\\\\")+)\"\\s*=\\s*\"((?:[^\"]|\\\\\")*)\"\\s*;"

    @Throws(IOException::class)
    override fun writeKeyValueString(key: String, value: String) {
        writeString("\"")
        writeString(key)
        writeString("\" = \"")
        writeString(value)
        writeString("\";")
    }
}