package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseResourcesConfig.Companion.DEFAULT_INTERFACE_NAME
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseResourcesConfig.Companion.DEFAULT_PACKAGE
import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinCommonResources
import ru.pocketbyte.locolaser.kotlinmpp.resource.file.provider.KotlinClassResourceFileProvider
import java.io.File

class KotlinCommonResourcesConfig(
    workDir: File?,
    resourceName: String?,
    resourcesDirPath: String?,
    filter: ((key: String) -> Boolean)?
) : BaseResourcesConfig(
    workDir,
    resourceName,
    resourcesDirPath,
    KotlinClassResourceFileProvider,
    filter
) {

    companion object : ResourcesConfigBuilderFactory<KotlinCommonResourcesConfig, KotlinCommonResourcesConfigBuilder> {
        const val TYPE = "kotlin-common"

        override fun getBuilder(): KotlinCommonResourcesConfigBuilder {
            return KotlinCommonResourcesConfigBuilder()
        }
    }

    override val type = TYPE

    override val defaultTempDirPath   = "./build/tmp/"
    override val defaultResourcesPath = "./build/generated/src/commonMain/kotlin/"
    override val defaultResourceName  = "$DEFAULT_PACKAGE.$DEFAULT_INTERFACE_NAME"

    override val resources by lazy {
        KotlinCommonResources(
            dir = this.resourcesDir,
            name = this.resourceName,
            resourceFileProvider = this.resourceFileProvider,
            filter = this.filter
        )
    }

}
