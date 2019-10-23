package ru.pocketbyte.locolaser.platform.mobile

import ru.pocketbyte.locolaser.platform.mobile.resource.IosObjectiveCResources

class IosObjectiveCPlatformConfig : IosBaseClassPlatformConfig() {

    companion object {
        const val TYPE = "ios_objc"
    }

    override val type = TYPE
    override val resources
        get() = IosObjectiveCResources(resourcesDir!!, resourceName!!, tableName, filter)

}
