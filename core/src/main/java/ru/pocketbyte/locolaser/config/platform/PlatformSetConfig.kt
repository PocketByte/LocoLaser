package ru.pocketbyte.locolaser.config.platform

import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.resource.PlatformResources
import ru.pocketbyte.locolaser.resource.PlatformSetResources

import java.io.File
import java.util.LinkedHashSet

class PlatformSetConfig(private val mConfigs: Set<PlatformConfig>) : Config.Child(), PlatformConfig {

    override val type: String = "set"

    override val resources: PlatformResources
        get() = PlatformSetResources(mConfigs.mapTo(LinkedHashSet(mConfigs.size)) { it.resources })

    override val defaultTempDir: File
        get() = mConfigs.first().defaultTempDir
}
