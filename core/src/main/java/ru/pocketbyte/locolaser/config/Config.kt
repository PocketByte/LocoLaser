/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config

import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.utils.buildFileFrom

import java.io.File
import java.io.Serializable

/**
 * Configuration object that contain information about localization rules.
 *
 * @author Denis Shurygin
 */
data class Config(
    val workDir: File? = null,

    /**
     * File from which this config was read.
     */
    val file: File? = null,

    /**
     * Source that contain resources.
     */
    val source: ResourcesConfig? = null,

    /**
     * Platform that contain logic of resource creation.
     */
    val platform: ResourcesConfig? = null,

    /**
     * Strategy that should be used for merge conflicts.
     * Default value: KEEP_NEW_PLATFORM.
     * @see [ru.pocketbyte.locolaser.config.Config.ConflictStrategy]
     */
    val conflictStrategy: ConflictStrategy = DEFAULT_CONFLICT_STRATEGY,

    /**
     * Set of locales that should be handled by LocoLaser.
     * You can use [Resources.BASE_LOCALE] to specify base locale.
     */
    val locales: Set<String> = DEFAULT_LOCALES,

    val extraParams: ExtraParams = ExtraParams()
): Serializable {

    enum class ConflictStrategy(private val strValue: String) {

        /** Remove platform resources and replace it with resources from source.  */
        REMOVE_PLATFORM("remove_platform"),

        /** Keep new platform resources if source doesn't contain this resources.  */
        KEEP_NEW_PLATFORM("keep_new_platform"),

        /** Keep platform resources even if source contain this resources.  */
        KEEP_PLATFORM("keep_platform"),

        /** New platform resources should be exported into source if source doesn't contain this resources.  */
        EXPORT_NEW_PLATFORM("export_new_platform"),

        /** Platform resources should be exported into source even if source contain this resources. */
        EXPORT_PLATFORM("export_platform");

        override fun toString(): String {
            return strValue
        }

        val isExportStrategy: Boolean
            get() = this == EXPORT_NEW_PLATFORM || this == EXPORT_PLATFORM
    }

    companion object {
        val DEFAULT_CONFLICT_STRATEGY = ConflictStrategy.KEEP_NEW_PLATFORM
        val DEFAULT_LOCALES = setOf(Resources.BASE_LOCALE)
    }

    /**
     * Defines if comment should be written even if it equal resource value.
     * True if comment should be written even if it equal resource value, false otherwise.
     */
    val duplicateComments: Boolean
        get() = extraParams.duplicateComments

    /**
     * Defines if unsupported quantities should be throw away if is not supported by locale.
     * True if unsupported quantities should be throw away, false otherwise.
     */
    val trimUnsupportedQuantities: Boolean
        get() = extraParams.trimUnsupportedQuantities

    fun allSourceFiles(): List<File> {
        return source?.resources?.allFiles(locales)
            ?: emptyList()
    }

    fun allPlatformFiles(): List<File> {
        return platform?.resources?.allFiles(locales)
            ?: emptyList()
    }
}