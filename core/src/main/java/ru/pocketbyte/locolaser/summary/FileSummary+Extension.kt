package ru.pocketbyte.locolaser.summary

operator fun FileSummary?.plus(increment: FileSummary?): FileSummary? {
    return when {
        this == null -> increment
        increment == null -> this
        else -> FileSummary(
            this.bytes + increment.bytes,
            this.hash + increment.hash
        )
    }
}