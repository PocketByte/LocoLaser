package ru.pocketbyte.locolaser.platform.mobile

import ru.pocketbyte.locolaser.platform.mobile.resource.IosSwiftResources
import ru.pocketbyte.locolaser.resource.PlatformResources

class IosSwiftPlatformConfig : IosBaseClassPlatformConfig() {

    companion object {
        const val TYPE = "ios_swift"
    }

    override val type = TYPE
    override val resources
        get() = IosSwiftResources(resourcesDir!!, resourceName!!, tableName, filter)

}
