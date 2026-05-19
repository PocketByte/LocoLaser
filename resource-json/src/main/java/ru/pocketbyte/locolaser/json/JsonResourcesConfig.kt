/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.json

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import ru.pocketbyte.locolaser.json.resource.JsonResources
import ru.pocketbyte.locolaser.json.resource.file.provider.JsonResourceFileProvider
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import java.io.File

/**
 * Resources configuration for the JSON localization format.
 */
class JsonResourcesConfig(
    workDir: File?,
    val indent: Int,
    val pluralKeyRule: KeyPluralizationRule.Postfix,
    resourceName: String?,
    resourcesDirPath: String?,
    val formattingType: FormattingType,
    resourceFileProvider: ResourceFileProvider?,
    filter: ResourcesFilter?
) : BaseResourcesConfig(
    workDir,
    resourceName,
    resourcesDirPath,
    resourceFileProvider ?: JsonResourceFileProvider,
    filter
) {

    companion object : ResourcesConfigBuilderFactory<JsonResourcesConfig, JsonResourcesConfigBuilder> {
        const val TYPE = "json"

        override fun getBuilder(): JsonResourcesConfigBuilder {
            return JsonResourcesConfigBuilder()
        }
    }

    override val type = TYPE

    override val defaultResourcesPath = "./locales/"
    override val defaultResourceName = "strings"

    override val resources: Resources by lazy {
        JsonResources(
            resourcesDir = this.resourcesDir,
            fileName = this.resourceName,
            formattingType = formattingType,
            resourceFileProvider = this.resourceFileProvider,
            indent = this.indent,
            pluralKeyRule = this.pluralKeyRule,
            filter = this.filter
        )
    }
}
