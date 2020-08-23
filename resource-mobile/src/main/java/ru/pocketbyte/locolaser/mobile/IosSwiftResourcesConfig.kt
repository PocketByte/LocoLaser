package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.mobile.resource.IosSwiftResources

class IosSwiftResourcesConfig : IosBaseClassResourcesConfig() {

    companion object {
        const val TYPE = "ios_swift"
    }

    override val type = TYPE
    override val resources
        get() = IosSwiftResources(resourcesDir!!, resourceName!!, tableName, filter)

}
