package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType

/**
 * Abstract builder for Kotlin Multiplatform resources configurations that support a configurable string formatting type.
 */
abstract class KotlinBaseCustomFormattingResourceConfigBuilder<out T : KotlinBaseResourcesConfig>
    : KotlinBaseResourcesConfigBuilder<T>(){

    /**
     * Formatting type applied to localized string values.
     * Determines how format arguments are substituted in generated code.
     * Defaults to [NoFormattingType].
     * @see FormattingType.ArgumentsSubstitution
     */
    var formattingType: FormattingType = NoFormattingType
}
