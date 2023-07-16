package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseImplResourcesConfig.Companion.DEFAULT_INTERFACE_NAME
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseImplResourcesConfig.Companion.DEFAULT_PACKAGE
import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinCommonResources

class KotlinCommonResourcesConfig : BaseResourcesConfig() {

    companion object {
        const val TYPE = "kotlin-common"
    }

    override val type = TYPE

    override val defaultTempDirPath   = "./build/tmp/"
    override val defaultResourcesPath = "./build/generated/src/commonMain/kotlin/"
    override val defaultResourceName  = "$DEFAULT_PACKAGE.$DEFAULT_INTERFACE_NAME"

    override val resources
        get() = KotlinCommonResources(resourcesDir, resourceName, filter)

}
