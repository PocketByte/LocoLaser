/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config

import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.resource.Resources

import java.io.File
import java.io.Serializable

/**
 * Immutable configuration for a single localization run, specifying the source,
 * target platform, locales, and conflict resolution strategy.
 *
 * @author Denis Shurygin
 */
data class Config(
    /**
     * Working directory used to resolve relative paths defined in the config.
     */
    val workDir: File? = null,

    /**
     * File from which this config was read.
     */
    val file: File? = null,

    /**
     * The resource source from which localization data is read (e.g., a remote spreadsheet or file).
     */
    val source: ResourcesConfig? = null,

    /**
     * The target platform to which localized resource files are written (e.g., Android, iOS).
     */
    val platform: ResourcesConfig? = null,

    /**
     * Strategy for resolving conflicts between platform and source resources during localization.
     * Default value: KEEP_NEW_PLATFORM.
     * @see [ru.pocketbyte.locolaser.config.Config.ConflictStrategy]
     */
    val conflictStrategy: ConflictStrategy = DEFAULT_CONFLICT_STRATEGY,

    /**
     * Set of locales that should be handled by LocoLaser.
     * You can use [Resources.BASE_LOCALE] to specify base locale.
     */
    val locales: Set<String> = DEFAULT_LOCALES,

    /**
     * Additional parameters passed to resources during read and write operations.
     */
    val extraParams: ExtraParams = ExtraParams()
): Serializable {

    /**
     * Defines the strategy used to resolve conflicts between platform resources and source resources.
     */
    enum class ConflictStrategy(private val strValue: String) {

        /** Removes platform resources and replaces them with resources from the source.  */
        REMOVE_PLATFORM("remove_platform"),

        /** Keeps new platform resources if the source does not contain these resources.  */
        KEEP_NEW_PLATFORM("keep_new_platform"),

        /** Keeps platform resources even if the source contains these resources.  */
        KEEP_PLATFORM("keep_platform"),

        /** Exports new platform resources into the source if the source does not contain these resources.  */
        EXPORT_NEW_PLATFORM("export_new_platform"),

        /** Exports platform resources into the source even if the source contains these resources. */
        EXPORT_PLATFORM("export_platform");

        override fun toString(): String {
            return strValue
        }

        /**
         * True if this strategy exports platform resources back to the source.
         */
        val isExportStrategy: Boolean
            get() = this == EXPORT_NEW_PLATFORM || this == EXPORT_PLATFORM
    }

    companion object {
        private const val serialVersionUID = 1L

        /** Default conflict strategy used when none is specified. */
        val DEFAULT_CONFLICT_STRATEGY = ConflictStrategy.KEEP_NEW_PLATFORM

        /** Default set of locales used when none is specified. Contains only the base locale. */
        val DEFAULT_LOCALES = setOf(Resources.BASE_LOCALE)
    }

    /**
     * Defines if a comment should be written even if it equals the resource value.
     */
    val duplicateComments: Boolean
        get() = extraParams.duplicateComments

    /**
     * Defines if unsupported quantities should be thrown away if they are not supported by the locale.
     */
    val trimUnsupportedQuantities: Boolean
        get() = extraParams.trimUnsupportedQuantities

    /**
     * Returns all resource files associated with the source for the configured locales.
     */
    fun allSourceFiles(): List<File> {
        return source?.resources?.allFiles(locales)
            ?: emptyList()
    }

    /**
     * Returns all resource files associated with the platform for the configured locales.
     */
    fun allPlatformFiles(): List<File> {
        return platform?.resources?.allFiles(locales)
            ?: emptyList()
    }
}