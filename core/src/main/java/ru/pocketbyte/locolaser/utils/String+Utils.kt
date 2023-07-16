package ru.pocketbyte.locolaser.utils


fun String?.firstCharToUpperCase(): String {
    return when {
        this?.isEmpty() != false -> ""
        length == 1 -> uppercase()
        else -> substring(0, 1).uppercase() + substring(1)
    }
}