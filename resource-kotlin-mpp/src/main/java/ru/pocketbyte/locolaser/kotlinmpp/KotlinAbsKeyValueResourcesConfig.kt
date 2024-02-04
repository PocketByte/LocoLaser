package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinAbsKeyValueResources
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import java.io.File

class KotlinAbsKeyValueResourcesConfig(
    workDir: File?,
    resourceName: String?,
    resourcesDirPath: String?,
    interfaceName: String?,
    override val formattingType: FormattingType = NoFormattingType,
    filter: ((key: String) -> Boolean)?
) : KotlinBaseResourcesConfig(
    workDir, resourceName, resourcesDirPath, interfaceName, filter
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

    override val resources by lazy {
        KotlinAbsKeyValueResources(
            dir = this.resourcesDir,
            name = this.resourceName,
            interfaceName = this.implements,
            formattingType = this.formattingType,
            resourceFileProvider = this.resourceFileProvider,
            filter = this.filter
        )
    }
}