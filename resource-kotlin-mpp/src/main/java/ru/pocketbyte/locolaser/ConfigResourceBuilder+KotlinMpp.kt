package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ConfigResourceBuilder
import ru.pocketbyte.locolaser.kotlinmpp.*

/**
 * Create and configure Kotlin Multiplatform Common config.
 * This config generates strings Repository interface for Common code.
 */
fun ConfigResourceBuilder.kotlinCommon(action: KotlinCommonResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = KotlinCommonResourcesConfig()
    action(KotlinCommonResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}

/**
 * Create and configure Kotlin Multiplatform Android config.
 * This config generates strings Repository implementation for Android platform.
 */
fun ConfigResourceBuilder.kotlinAndroid(action: KotlinAndroidResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = KotlinAndroidResourcesConfig()
    action(KotlinAndroidResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}

/**
 * Create and configure Kotlin Multiplatform iOS config.
 * This config generates strings Repository implementation for iOS platform.
 */
fun ConfigResourceBuilder.kotlinIos(action: KotlinIosResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = KotlinIosResourcesConfig()
    action(KotlinIosResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}

/**
 * Create and configure Kotlin Multiplatform JS config.
 * This config generates strings Repository implementation for JS platform.
 */
fun ConfigResourceBuilder.kotlinJs(action: KotlinJsResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = KotlinJsResourcesConfig()
    action(KotlinJsResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}

/**
 * Create and configure Kotlin Multiplatform Abstract config.
 * This config generates strings Repository implementation with abstract implementation, that can be used in any target.
 */
fun ConfigResourceBuilder.kotlinAbsKeyValue(action: KotlinAbsKeyValueResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = KotlinAbsKeyValueResourcesConfig()
    action(KotlinAbsKeyValueResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}

/**
 * Create and configure Kotlin Multiplatform config.
 * This config generates strings Repository interface ant it's implementation.
 */
fun ConfigResourceBuilder.kotlinMultiplatform(action: KotlinMultiplatformResourcesConfigBuilder.() -> Unit) {
    val multiplatformConfig = KotlinMultiplatformResourcesConfigBuilder()
    action(multiplatformConfig)
    add(multiplatformConfig.build())
}