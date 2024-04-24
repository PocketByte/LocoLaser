/*
 * Copyright (c) New Cloud Technologies, Ltd., 2013-2024
 *
 * You can not use the contents of the file in any way without New Cloud Technologies, Ltd. written permission.
 * To obtain such a permit, you should contact New Cloud Technologies, Ltd. at https://myoffice.ru/contacts/
 *
 */

package ru.pocketbyte.locolaser.kotlinmpp.extension

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

val Project.kotlin: KotlinMultiplatformExtension?
    get() = extensions.findByName("kotlin") as? KotlinMultiplatformExtension

fun Project.kotlin(configure: KotlinMultiplatformExtension.() -> Unit) {
    extensions.configure("kotlin", configure)
}