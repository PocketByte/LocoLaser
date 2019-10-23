package ru.pocketbyte.locolaser.platform.kotlinmpp

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.platform.kotlinmpp.resource.KotlinCommonResources

class KotlinCommonPlatformConfig : BasePlatformConfig() {

    companion object {
        const val TYPE = "kotlin-common"
    }

    override val type = TYPE

    override val defaultTempDirPath   = "./build/tmp/"
    override val defaultResourcesPath = "./src/commonMain/kotlin/"
    override val defaultResourceName  = "ru.pocketbyte.locolaser.kmpp.StringRepository"

    override val resources
        get() = KotlinCommonResources(resourcesDir!!, resourceName!!, filter)

}
