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
 * Creates and adds an Android resources configuration to this resources set.
 *
 * @param action Configuration block applied to the [AndroidResourcesConfigBuilder].
 */
fun ResourcesSetConfigBuilder.android(action: AndroidResourcesConfigBuilder.() -> Unit) {
    add(AndroidResourcesConfig, action)
}

/**
 * Creates and adds a default Android resources configuration to this resources set.
 */
fun ResourcesSetConfigBuilder.android() {
    add(AndroidResourcesConfig)
}

/**
 * Creates and adds an iOS resources configuration to this resources set.
 *
 * @param action Configuration block applied to the [IosResourcesConfigBuilder].
 */
fun ResourcesSetConfigBuilder.ios(action: IosResourcesConfigBuilder.() -> Unit) {
    add(IosResourcesConfig, action)
}

/**
 * Creates and adds a default iOS resources configuration to this resources set.
 */
fun ResourcesSetConfigBuilder.ios() {
    add(IosResourcesConfig)
}

/**
 * Creates and adds an iOS Plist resources configuration to this resources set.
 *
 * @param action Configuration block applied to the [IosPlistResourcesConfigBuilder].
 */
fun ResourcesSetConfigBuilder.iosPlist(action: IosPlistResourcesConfigBuilder.() -> Unit) {
    add(IosPlistResourcesConfig, action)
}

/**
 * Creates and adds an iOS Objective-C class generator configuration to this resources set.
 *
 * @param action Configuration block applied to the [IosObjectiveCResourcesConfigBuilder].
 */
fun ResourcesSetConfigBuilder.iosObjC(action: IosObjectiveCResourcesConfigBuilder.() -> Unit) {
    add(IosObjectiveCResourcesConfig ,action)
}

/**
 * Creates and adds an iOS Swift class generator configuration to this resources set.
 *
 * @param action Configuration block applied to the [IosSwiftResourcesConfigBuilder].
 */
fun ResourcesSetConfigBuilder.iosSwift(action: IosSwiftResourcesConfigBuilder.() -> Unit) {
    add(IosSwiftResourcesConfig, action)
}
