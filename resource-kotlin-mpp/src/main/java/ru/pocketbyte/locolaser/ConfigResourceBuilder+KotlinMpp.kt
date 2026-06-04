package ru.pocketbyte.locolaser

import org.gradle.api.Project
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
 * Creates and adds a Kotlin Multiplatform Common resources configuration to this resources set.
 *
 * Generates a strings repository interface for common (shared) code.
 *
 * @param action Configuration block applied to the [KotlinCommonResourcesConfigBuilder].
 */
@Deprecated("Use kotlinMultiplatform.common instead.", level = DeprecationLevel.WARNING)
fun ResourcesSetConfigBuilder.kotlinCommon(
    action: KotlinCommonResourcesConfigBuilder.() -> Unit
) {
    add(KotlinCommonResourcesConfigBuilder(), action)
}

/**
 * Creates and adds a Kotlin Multiplatform Android resources configuration to this resources set.
 *
 * Generates a strings repository implementation for the Android platform.
 *
 * @param action Configuration block applied to the [KotlinAndroidResourcesConfigBuilder].
 */
@Deprecated("Use kotlinMultiplatform.android instead.", level = DeprecationLevel.WARNING)
fun ResourcesSetConfigBuilder.kotlinAndroid(
    action: KotlinAndroidResourcesConfigBuilder.() -> Unit
) {
    add(KotlinAndroidResourcesConfigBuilder(), action)
}

/**
 * Creates and adds a Kotlin Multiplatform iOS resources configuration to this resources set.
 *
 * Generates a strings repository implementation for the iOS platform.
 *
 * @param action Configuration block applied to the [KotlinIosResourcesConfigBuilder].
 */
@Deprecated("Use kotlinMultiplatform.ios instead.", level = DeprecationLevel.WARNING)
fun ResourcesSetConfigBuilder.kotlinIos(
    action: KotlinIosResourcesConfigBuilder.() -> Unit
) {
    add(KotlinIosResourcesConfigBuilder(), action)
}

/**
 * Creates and adds a Kotlin Multiplatform JS resources configuration to this resources set.
 *
 * Generates a strings repository implementation for the JS platform.
 *
 * @param action Configuration block applied to the [KotlinJsResourcesConfigBuilder].
 */
@Deprecated("Use kotlinMultiplatform.js instead.", level = DeprecationLevel.WARNING)
fun ResourcesSetConfigBuilder.kotlinJs(
    action: KotlinJsResourcesConfigBuilder.() -> Unit
) {
    add(KotlinJsResourcesConfigBuilder(), action)
}

/**
 * Creates and adds a Kotlin Multiplatform AbsKeyValue resources configuration to this resources set.
 *
 * Generates an abstract key-value strings repository implementation usable in any target.
 *
 * @param action Configuration block applied to the [KotlinAbsKeyValueResourcesConfigBuilder].
 */
@Deprecated("Use kotlinMultiplatform.absKeyValue instead.", level = DeprecationLevel.WARNING)
fun ResourcesSetConfigBuilder.kotlinAbsKeyValue(
    action: KotlinAbsKeyValueResourcesConfigBuilder.() -> Unit
) {
    add(KotlinAbsKeyValueResourcesConfigBuilder(), action)
}

/**
 * Creates and adds a Kotlin Multiplatform AbsStatic resources configuration to this resources set.
 *
 * Generates an abstract static strings repository implementation usable in any target.
 * Particularly useful for testing, as it provides a simple static implementation of the repository.
 *
 * @param action Configuration block applied to the [KotlinAbsStaticResourcesConfigBuilder].
 */
@Deprecated("Use kotlinMultiplatform.absStatic instead.", level = DeprecationLevel.WARNING)
fun ResourcesSetConfigBuilder.kotlinAbsStatic(
    action: KotlinAbsStaticResourcesConfigBuilder.() -> Unit
) {
    add(KotlinAbsStaticResourcesConfigBuilder(), action)
}

/**
 * Creates and adds a Kotlin Multiplatform AbsProxy resources configuration to this resources set.
 *
 * Generates an abstract proxy strings repository implementation usable in any target.
 *
 * @param action Configuration block applied to the [KotlinAbsProxyResourcesConfigBuilder].
 */
@Deprecated("Use kotlinMultiplatform.absProxy instead.", level = DeprecationLevel.WARNING)
fun ResourcesSetConfigBuilder.kotlinAbsProxy(
    action: KotlinAbsProxyResourcesConfigBuilder.() -> Unit
) {
    add(KotlinAbsProxyResourcesConfigBuilder(), action)
}

/**
 * Creates and adds a Kotlin Multiplatform resources configuration to this resources set.
 *
 * Generates a strings repository interface and its platform implementations.
 *
 * @param project If provided, generated files will be registered in Kotlin source sets of the project.
 * @param action Configuration block applied to the [KotlinMultiplatformResourcesConfigBuilder].
 */
fun ResourcesSetConfigBuilder.kotlinMultiplatform(
    project: Project? = null,
    action: KotlinMultiplatformResourcesConfigBuilder.() -> Unit
) {
    add(KotlinMultiplatformResourcesConfigBuilder(project), action)
}
