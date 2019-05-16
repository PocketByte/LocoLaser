package ru.pocketbyte.locolaser.platform.mobile

import ru.pocketbyte.locolaser.platform.mobile.resource.IosObjectiveCResources
import ru.pocketbyte.locolaser.resource.PlatformResources

class IosObjectiveCPlatformConfig : IosBaseClassPlatformConfig() {

    companion object {
        const val TYPE = "ios_objc"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getResources(): PlatformResources {
        return IosObjectiveCResources(resourcesDir, resourceName, tableName)
    }

}
