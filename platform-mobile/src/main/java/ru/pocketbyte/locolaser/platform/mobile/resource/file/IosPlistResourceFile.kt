package ru.pocketbyte.locolaser.platform.mobile.resource.file

import java.io.File
import java.io.IOException

class IosPlistResourceFile(file: File, locale: String) : AbsIosStringsResourceFile(file, locale) {

    override val keyValueLinePattern = "^\\s?((?:[^\"]|\\\\\")+?)\\s*=\\s*\"((?:[^\"]|\\\\\")*)\"\\s*;"

    @Throws(IOException::class)
    override fun writeKeyValueString(key: String, value: String) {
        writeString(key)
        writeString(" = \"")
        writeString(value)
        writeString("\";")
    }
}