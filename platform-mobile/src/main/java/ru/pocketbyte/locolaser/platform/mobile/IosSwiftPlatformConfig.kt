package ru.pocketbyte.locolaser.platform.mobile

import ru.pocketbyte.locolaser.platform.mobile.resource.IosSwiftResources
import ru.pocketbyte.locolaser.resource.PlatformResources

class IosSwiftPlatformConfig : IosBaseClassPlatformConfig() {

    companion object {
        const val TYPE = "ios_swift"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getResources(): PlatformResources {
        return IosSwiftResources(resourcesDir, resourceName, tableName)
    }

}
