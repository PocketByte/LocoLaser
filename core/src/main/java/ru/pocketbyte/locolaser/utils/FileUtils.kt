package ru.pocketbyte.locolaser.utils

import java.io.File

/**
 * Builds a [File] from [path], resolving it relative to [workDir] if [path] starts with `"."`.
 * Otherwise returns `File(path)` regardless of [workDir].
 *
 * @param workDir The working directory used as the base for relative paths, or null to always use [path] as-is.
 * @param path The file path, either absolute or relative (starting with `"."`).
 */
fun buildFileFrom(workDir: File?, path: String): File {
    return if (workDir != null && path.startsWith(".")) {
        File(workDir, path)
    } else {
        File(path)
    }
}
