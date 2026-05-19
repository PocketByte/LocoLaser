package ru.pocketbyte.locolaser.kotlinmpp.builder

import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseCustomFormattingResourceConfigBuilder
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseResourcesConfig
import ru.pocketbyte.locolaser.resource.formatting.FormattingType

internal typealias CustomFormattingClassBuilder
        = KotlinBaseCustomFormattingResourceConfigBuilder<KotlinBaseResourcesConfig>

internal typealias CustomFormattingClassBuilderFactory
        = ResourcesConfigBuilderFactory<KotlinBaseResourcesConfig, CustomFormattingClassBuilder>

/**
 * Builder for Kotlin Multiplatform platform-specific strings repository class configurations
 * that support a configurable string formatting type.
 */
class KmpClassCustomFormattingBuilder(
    name: String,
    builderFactory: CustomFormattingClassBuilderFactory,
) : BaseKmpClassBuilder<KotlinBaseResourcesConfig, CustomFormattingClassBuilder>(name, builderFactory) {

    /**
     * Formatting type applied to localized string values in the generated repository class.
     * If `null`, the formatting type from the parent builder is used.
     */
    var formattingType: FormattingType? = null

    override fun configure(builder: CustomFormattingClassBuilder) {
        super.configure(builder)

        this.formattingType?.let {
            builder.formattingType = it
        }
    }
}
