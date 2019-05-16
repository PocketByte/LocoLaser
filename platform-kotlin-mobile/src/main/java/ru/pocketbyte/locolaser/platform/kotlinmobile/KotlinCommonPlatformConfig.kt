package ru.pocketbyte.locolaser.platform.kotlinmobile

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.platform.kotlinmobile.resource.KotlinCommonResources
import ru.pocketbyte.locolaser.resource.PlatformResources

class KotlinCommonPlatformConfig : BasePlatformConfig() {

    companion object {
        const val TYPE = "kotlin-common"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getDefaultTempDirPath(): String {
        return "./build/tmp/"
    }

    override fun getDefaultResourcesPath(): String {
        return "./src/main/kotlin/"
    }

    override fun getDefaultResourceName(): String {
        return "ru.pocketbyte.locolaser.StrRepository"
    }

    override fun getResources(): PlatformResources {
        return KotlinCommonResources(resourcesDir, resourceName)
    }
}
