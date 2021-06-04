package ru.pocketbyte.locolaser.plugin.utils


fun String?.firstCharToUpperCase(): String {
    return when {
        this?.isEmpty() != false -> ""
        length == 1 -> toUpperCase()
        else -> substring(0, 1).toUpperCase() + substring(1)
    }
}