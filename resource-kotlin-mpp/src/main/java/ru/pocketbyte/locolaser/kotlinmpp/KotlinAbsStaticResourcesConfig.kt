package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinAbsStaticResources
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType

class KotlinAbsStaticResourcesConfig(
    resourceName: String?,
    resourcesDirPath: String?,
    interfaceName: String?,
    override val formattingType: FormattingType = NoFormattingType,
    filter: ((key: String) -> Boolean)?
) : KotlinBaseResourcesConfig(
    resourceName, resourcesDirPath, interfaceName, filter
), KotlinResourcesConfigWithFormattingType {

    companion object : ResourcesConfigBuilderFactory<KotlinAbsStaticResourcesConfig, KotlinAbsStaticResourcesConfigBuilder> {
        const val TYPE = "kotlin-abs-static"

        override fun getBuilder(): KotlinAbsStaticResourcesConfigBuilder {
            return KotlinAbsStaticResourcesConfigBuilder()
        }
    }

    override val type = TYPE

    override val defaultResourcesPath = "./build/generated/src/commonMain/kotlin/"
    override val defaultResourceName  = "$DEFAULT_PACKAGE.AbsStatic$DEFAULT_INTERFACE_NAME"

    override val resources
        get() = KotlinAbsStaticResources(
            resourcesDir, resourceName, implements,
            formattingType, resourceFileProvider, filter
        )
}