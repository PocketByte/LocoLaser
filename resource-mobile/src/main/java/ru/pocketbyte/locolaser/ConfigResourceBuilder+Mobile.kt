package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.ConfigResourceBuilder
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder

fun ConfigResourceBuilder.android(action: BaseResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = AndroidResourcesConfig()
    action(BaseResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}

fun ConfigResourceBuilder.android() {
    add(AndroidResourcesConfig())
}

fun ConfigResourceBuilder.ios(action: BaseResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = IosResourcesConfig()
    action(BaseResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}

fun ConfigResourceBuilder.ios() {
    add(IosResourcesConfig())
}

fun ConfigResourceBuilder.iosPlist(action: BaseResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = IosPlistResourcesConfig()
    action(BaseResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}

fun ConfigResourceBuilder.iosObjC(action: IosClassResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = IosObjectiveCResourcesConfig()
    action(IosClassResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}

fun ConfigResourceBuilder.iosSwift(action: IosClassResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = IosSwiftResourcesConfig()
    action(IosClassResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}