package ru.pocketbyte.locolaser.json

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.WebFormattingType
import java.io.File

/**
 * Builder for [JsonResourcesConfig].
 *
 * Provides configuration options for JSON-based localization resources,
 * including indentation, plural key rules, and string formatting type.
 */
open class JsonResourcesConfigBuilder : BaseResourcesConfigBuilder<JsonResourcesConfig>() {

    /**
     * Number of spaces used for JSON indentation.
     * Set to `-1` to produce compact (non-prettified) JSON.
     */
    var indent: Int = -1

    /**
     * Rule used to encode and decode plural string keys in the JSON file.
     * For example, older versions of i18next use [KeyPluralizationRule.Postfix.Numeric],
     * while newer versions use [KeyPluralizationRule.Postfix.Named].
     */
    var pluralKeyRule: KeyPluralizationRule.Postfix = KeyPluralizationRule.Postfix.Named()

    /**
     * Formatting type applied to localized string values.
     * Defaults to [WebFormattingType].
     */
    var formattingType: FormattingType = WebFormattingType

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ResourcesFilter?
    ): JsonResourcesConfig {
        return JsonResourcesConfig(
            workDir,
            indent, pluralKeyRule,
            resourceName, resourcesDir,
            formattingType,
            resourceFileProvider, filter
        )
    }

}
