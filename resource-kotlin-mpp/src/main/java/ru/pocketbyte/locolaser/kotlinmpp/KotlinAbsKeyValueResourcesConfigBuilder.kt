package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.resource.formatting.FormattingType

class KotlinAbsKeyValueResourcesConfigBuilder(
        private val config: KotlinAbsKeyValueResourcesConfig
): KotlinBaseImplResourcesConfigBuilder(config) {

    var formattingType: FormattingType
        get() = config.formattingType
        set(value) { config.formattingType = value }

}