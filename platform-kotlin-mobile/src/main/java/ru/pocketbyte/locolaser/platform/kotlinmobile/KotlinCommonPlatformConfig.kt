package ru.pocketbyte.locolaser.platform.kotlinmobile

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.platform.kotlinmobile.resource.KotlinCommonResources

class KotlinCommonPlatformConfig : BasePlatformConfig() {

    companion object {
        const val TYPE = "kotlin-common"
    }

    override val type = TYPE

    override val defaultTempDirPath = "./build/tmp/"
    override val defaultResourcesPath = "./src/main/kotlin/"
    override val defaultResourceName = "ru.pocketbyte.locolaser.StrRepository"

    override val resources = KotlinCommonResources(resourcesDir!!, resourceName!!)

}
