package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinAbsKeyValueResources
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType

class KotlinAbsKeyValueResourcesConfig(
    resourceName: String?,
    resourcesDirPath: String?,
    interfaceName: String?,
    override val formattingType: FormattingType = NoFormattingType,
    filter: ((key: String) -> Boolean)?
) : KotlinBaseResourcesConfig(
    resourceName, resourcesDirPath, interfaceName, filter
), KotlinResourcesConfigWithFormattingType {

    companion object : ResourcesConfigBuilderFactory<KotlinAbsKeyValueResourcesConfig, KotlinAbsKeyValueResourcesConfigBuilder> {
        const val TYPE = "kotlin-abs-key-value"
        override fun getBuilder(): KotlinAbsKeyValueResourcesConfigBuilder {
            return KotlinAbsKeyValueResourcesConfigBuilder()
        }
    }

    override val type = TYPE

    override val defaultResourcesPath = "./build/generated/src/main/kotlin/"
    override val defaultResourceName  = "$DEFAULT_PACKAGE.AbsKeyValue$DEFAULT_INTERFACE_NAME"

    override val resources
        get() = KotlinAbsKeyValueResources(
            resourcesDir, resourceName, implements,
            formattingType, resourceFileProvider, filter
        )
}