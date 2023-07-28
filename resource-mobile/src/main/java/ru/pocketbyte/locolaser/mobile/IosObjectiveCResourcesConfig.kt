package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.mobile.resource.IosObjectiveCResources

class IosObjectiveCResourcesConfig : IosBaseClassResourcesConfig() {

    companion object {
        const val TYPE = "ios_objc"
    }

    override val type = TYPE

    override val resources
        get() = IosObjectiveCResources(
            resourcesDir, resourceName, resourceFileProvider,
            tableName, filter
        )

}
