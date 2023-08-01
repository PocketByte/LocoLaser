package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.resource.formatting.FormattingType

class KotlinAbsStaticResourcesConfigBuilder
    : KotlinBaseImplResourcesConfigBuilder<KotlinAbsStaticResourcesConfig>(
    KotlinAbsStaticResourcesConfig()
) {

    /**
     * Values formatting type.
     * Depending on formatting arguments substitution type will be generated various implementations.
     * @see FormattingType.ArgumentsSubstitution
     */
    var formattingType: FormattingType
        get() = config.formattingType
        set(value) { config.formattingType = value }

}