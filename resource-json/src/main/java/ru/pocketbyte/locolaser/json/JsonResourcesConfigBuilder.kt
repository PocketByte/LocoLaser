package ru.pocketbyte.locolaser.json

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider

open class JsonResourcesConfigBuilder : BaseResourcesConfigBuilder<JsonResourcesConfig>() {

    /**
     * JSON indent. Set this property to prettify result JSON.
     */
    var indent: Int = -1

    /**
     * Defines which pluralization rule should be used to define plural string keys.
     * For example, old version of i18next uses NUMERIC_POSTFIX but latest NAMED_POSTFIX
     */
    var pluralKeyRule: KeyPluralizationRule.Postfix = KeyPluralizationRule.Postfix.Named()

    override fun buildConfig(
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): JsonResourcesConfig {
        return JsonResourcesConfig(
            indent, pluralKeyRule,
            resourceName, resourcesDir,
            resourceFileProvider, filter
        )
    }

}