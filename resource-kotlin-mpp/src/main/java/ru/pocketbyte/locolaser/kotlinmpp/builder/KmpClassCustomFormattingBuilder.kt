package ru.pocketbyte.locolaser.kotlinmpp.builder

import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseCustomFormattingResourceConfigBuilder
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseResourcesConfig
import ru.pocketbyte.locolaser.resource.formatting.FormattingType

internal typealias CustomFormattingClassBuilder
        = KotlinBaseCustomFormattingResourceConfigBuilder<KotlinBaseResourcesConfig>

internal typealias CustomFormattingClassBuilderFactory
        = ResourcesConfigBuilderFactory<KotlinBaseResourcesConfig, CustomFormattingClassBuilder>

class KmpClassCustomFormattingBuilder(
    name: String,
    builderFactory: CustomFormattingClassBuilderFactory,
) : BaseKmpClassBuilder<KotlinBaseResourcesConfig, CustomFormattingClassBuilder>(name, builderFactory) {

    var formattingType: FormattingType? = null

    override fun configure(builder: CustomFormattingClassBuilder) {
        super.configure(builder)

        this.formattingType?.let {
            builder.formattingType = it
        }
    }
}