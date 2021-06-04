package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ConfigResourceBuilder
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.mobile.*


/**
 * Create and configure Android resources config.
 */
fun ConfigResourceBuilder.android(action: BaseResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = AndroidResourcesConfig()
    action(AndroidResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}

/**
 * Create default Android resources config.
 */
fun ConfigResourceBuilder.android() {
    add(AndroidResourcesConfig())
}

/**
 * Create and configure iOS resources config.
 */
fun ConfigResourceBuilder.ios(action: BaseResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = IosResourcesConfig()
    action(IosResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}

/**
 * Create default iOS resources config.
 */
fun ConfigResourceBuilder.ios() {
    add(IosResourcesConfig())
}

/**
 * Create and configure iOS Plist resources config.
 */
fun ConfigResourceBuilder.iosPlist(action: BaseResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = IosPlistResourcesConfig()
    action(IosPlistResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}

/**
 * Create and configure iOS Objective C class generator config.
 */
fun ConfigResourceBuilder.iosObjC(action: IosClassResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = IosObjectiveCResourcesConfig()
    action(IosObjectiveCResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}


/**
 * Create and configure iOS Swift class generator config.
 */
fun ConfigResourceBuilder.iosSwift(action: IosClassResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = IosSwiftResourcesConfig()
    action(IosSwiftResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}