package ru.pocketbyte.locolaser.utils

import java.io.File

fun buildFileFrom(workDir: File, path: String): File {
    return if (path.startsWith(".")) {
        File(workDir, path)
    } else {
        File(path)
    }
}