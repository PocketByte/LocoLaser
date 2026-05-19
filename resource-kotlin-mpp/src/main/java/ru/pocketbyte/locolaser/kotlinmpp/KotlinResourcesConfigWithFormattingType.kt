package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.resource.formatting.FormattingType

/** Marker interface for Kotlin Multiplatform resources configurations that expose a [FormattingType]. */
interface KotlinResourcesConfigWithFormattingType {
    /** Formatting type applied to localized string values in generated code. */
    val formattingType: FormattingType
}
