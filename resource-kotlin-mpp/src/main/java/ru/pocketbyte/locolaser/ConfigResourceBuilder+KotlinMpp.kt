package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ConfigResourceBuilder
import ru.pocketbyte.locolaser.kotlinmpp.*

fun ConfigResourceBuilder.kotlinCommon(action: KotlinCommonResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = KotlinCommonResourcesConfig()
    action(KotlinCommonResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}

fun ConfigResourceBuilder.kotlinAndroid(action: KotlinAndroidResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = KotlinAndroidResourcesConfig()
    action(KotlinAndroidResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}

fun ConfigResourceBuilder.kotlinIos(action: KotlinIosResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = KotlinIosResourcesConfig()
    action(KotlinIosResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}

fun ConfigResourceBuilder.kotlinJs(action: KotlinJsResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = KotlinJsResourcesConfig()
    action(KotlinJsResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}

fun ConfigResourceBuilder.kotlinAbsKeyValue(action: KotlinAbsKeyValueResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = KotlinAbsKeyValueResourcesConfig()
    action(KotlinAbsKeyValueResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}

fun ConfigResourceBuilder.kotlinMultiplatform(action: KotlinMultiplatformResourcesConfigBuilder.() -> Unit) {
    val multiplatformConfig = KotlinMultiplatformResourcesConfigBuilder()
    action(multiplatformConfig)
    add(multiplatformConfig.toResourcesConfig())
}