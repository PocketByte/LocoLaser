package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ResourcesSetConfigBuilder
import ru.pocketbyte.locolaser.kotlinmpp.KotlinAbsKeyValueResourcesConfigBuilder
import ru.pocketbyte.locolaser.kotlinmpp.KotlinAbsProxyResourcesConfigBuilder
import ru.pocketbyte.locolaser.kotlinmpp.KotlinAbsStaticResourcesConfigBuilder
import ru.pocketbyte.locolaser.kotlinmpp.KotlinAndroidResourcesConfigBuilder
import ru.pocketbyte.locolaser.kotlinmpp.KotlinCommonResourcesConfigBuilder
import ru.pocketbyte.locolaser.kotlinmpp.KotlinIosResourcesConfigBuilder
import ru.pocketbyte.locolaser.kotlinmpp.KotlinJsResourcesConfigBuilder
import ru.pocketbyte.locolaser.kotlinmpp.KotlinMultiplatformResourcesConfigBuilder

/**
 * Create and configure Kotlin Multiplatform Common config.
 * This config generates strings Repository interface for Common code.
 */
fun ResourcesSetConfigBuilder.kotlinCommon(
    action: KotlinCommonResourcesConfigBuilder.() -> Unit
) {
    add(KotlinCommonResourcesConfigBuilder(), action)
}

/**
 * Create and configure Kotlin Multiplatform Android config.
 * This config generates strings Repository implementation for Android platform.
 */
fun ResourcesSetConfigBuilder.kotlinAndroid(
    action: KotlinAndroidResourcesConfigBuilder.() -> Unit
) {
    add(KotlinAndroidResourcesConfigBuilder(), action)
}

/**
 * Create and configure Kotlin Multiplatform iOS config.
 * This config generates strings Repository implementation for iOS platform.
 */
fun ResourcesSetConfigBuilder.kotlinIos(
    action: KotlinIosResourcesConfigBuilder.() -> Unit
) {
    add(KotlinIosResourcesConfigBuilder(), action)
}

/**
 * Create and configure Kotlin Multiplatform JS config.
 * This config generates strings Repository implementation for JS platform.
 */
fun ResourcesSetConfigBuilder.kotlinJs(
    action: KotlinJsResourcesConfigBuilder.() -> Unit
) {
    add(KotlinJsResourcesConfigBuilder(), action)
}

/**
 * Create and configure Kotlin Multiplatform AbsKeyValue config.
 * This config generates abstract strings Repository implementation, that can be used in any target.
 */
fun ResourcesSetConfigBuilder.kotlinAbsKeyValue(
    action: KotlinAbsKeyValueResourcesConfigBuilder.() -> Unit
) {
    add(KotlinAbsKeyValueResourcesConfigBuilder(), action)
}

/**
 * Create and configure Kotlin Multiplatform AbsStatic config.
 * This config generates abstract strings Repository implementation, that can be used in any target.
 */
fun ResourcesSetConfigBuilder.kotlinAbsStatic(
    action: KotlinAbsStaticResourcesConfigBuilder.() -> Unit
) {
    add(KotlinAbsStaticResourcesConfigBuilder(), action)
}

/**
 * Create and configure Kotlin Multiplatform AbsProxy config.
 * This config generates abstract strings Repository implementation, that can be used in any target.
 */
fun ResourcesSetConfigBuilder.kotlinAbsProxy(
    action: KotlinAbsProxyResourcesConfigBuilder.() -> Unit
) {
    add(KotlinAbsProxyResourcesConfigBuilder(), action)
}

/**
 * Create and configure Kotlin Multiplatform config.
 * This config generates strings Repository interface ant it's implementation.
 */
fun ResourcesSetConfigBuilder.kotlinMultiplatform(
    action: KotlinMultiplatformResourcesConfigBuilder.() -> Unit
) {
    add(KotlinMultiplatformResourcesConfigBuilder(), action)
}