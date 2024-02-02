package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType

abstract class KotlinBaseCustomFormattingResourceConfigBuilder<out T : KotlinBaseResourcesConfig>
    : KotlinBaseResourcesConfigBuilder<T>(){

    /**
     * Values formatting type.
     * Depending on formatting arguments substitution type will be generated various implementations.
     * @see FormattingType.ArgumentsSubstitution
     */
    var formattingType: FormattingType = NoFormattingType
}