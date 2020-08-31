package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinCommonResources

class KotlinCommonResourcesConfig : BaseResourcesConfig() {

    companion object {
        const val TYPE = "kotlin-common"
    }

    override val type = ru.pocketbyte.locolaser.kotlinmpp.KotlinCommonResourcesConfig.Companion.TYPE

    override val defaultTempDirPath   = "./build/tmp/"
    override val defaultResourcesPath = "./src/commonMain/kotlin/"
    override val defaultResourceName  = "ru.pocketbyte.locolaser.kmpp.StringRepository"

    override val resources
        get() = KotlinCommonResources(resourcesDir!!, resourceName!!, filter)

}
