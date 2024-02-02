package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ResourcesSetConfigBuilder
import ru.pocketbyte.locolaser.mobile.AndroidResourcesConfig
import ru.pocketbyte.locolaser.mobile.AndroidResourcesConfigBuilder
import ru.pocketbyte.locolaser.mobile.IosObjectiveCResourcesConfig
import ru.pocketbyte.locolaser.mobile.IosObjectiveCResourcesConfigBuilder
import ru.pocketbyte.locolaser.mobile.IosPlistResourcesConfig
import ru.pocketbyte.locolaser.mobile.IosPlistResourcesConfigBuilder
import ru.pocketbyte.locolaser.mobile.IosResourcesConfig
import ru.pocketbyte.locolaser.mobile.IosResourcesConfigBuilder
import ru.pocketbyte.locolaser.mobile.IosSwiftResourcesConfig
import ru.pocketbyte.locolaser.mobile.IosSwiftResourcesConfigBuilder


/**
 * Create and configure Android resources config.
 */
fun ResourcesSetConfigBuilder.android(action: AndroidResourcesConfigBuilder.() -> Unit) {
    add(AndroidResourcesConfig, action)
}

/**
 * Create default Android resources config.
 */
fun ResourcesSetConfigBuilder.android() {
    add(AndroidResourcesConfig)
}

/**
 * Create and configure iOS resources config.
 */
fun ResourcesSetConfigBuilder.ios(action: IosResourcesConfigBuilder.() -> Unit) {
    add(IosResourcesConfig, action)
}

/**
 * Create default iOS resources config.
 */
fun ResourcesSetConfigBuilder.ios() {
    add(IosResourcesConfig)
}

/**
 * Create and configure iOS Plist resources config.
 */
fun ResourcesSetConfigBuilder.iosPlist(action: IosPlistResourcesConfigBuilder.() -> Unit) {
    add(IosPlistResourcesConfig, action)
}

/**
 * Create and configure iOS Objective C class generator config.
 */
fun ResourcesSetConfigBuilder.iosObjC(action: IosObjectiveCResourcesConfigBuilder.() -> Unit) {
    add(IosObjectiveCResourcesConfig ,action)
}


/**
 * Create and configure iOS Swift class generator config.
 */
fun ResourcesSetConfigBuilder.iosSwift(action: IosSwiftResourcesConfigBuilder.() -> Unit) {
    add(IosSwiftResourcesConfig, action)
}