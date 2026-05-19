package ru.pocketbyte.locolaser.kotlinmpp.builder

import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseResourcesConfig
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseResourcesConfigBuilder

internal typealias FixedFormattingClassBuilder
        = KotlinBaseResourcesConfigBuilder<KotlinBaseResourcesConfig>

internal typealias FixedFormattingClassBuilderFactory
        = ResourcesConfigBuilderFactory<KotlinBaseResourcesConfig, FixedFormattingClassBuilder>

/**
 * Builder for Kotlin Multiplatform platform-specific strings repository class configurations
 * with a fixed (non-configurable) string formatting type.
 */
open class KmpClassFixedFormattingBuilder(
    name: String,
    builderFactory: FixedFormattingClassBuilderFactory,
) : BaseKmpClassBuilder<KotlinBaseResourcesConfig, FixedFormattingClassBuilder>(
    name, builderFactory
)
