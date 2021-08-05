/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config

import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.resource.Resources

import java.io.File

/**
 * Configuration object that contain information about localization rules.
 *
 * @author Denis Shurygin
 */
class Config {

    open class Child {
        var parent: Config? = null
    }

    enum class ConflictStrategy private constructor(val strValue: String) {

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

    /**
     * File from which this config was read.
     */
    var file: File? = null
    /**
     * Source that contain resources.
     */
    var source: ResourcesConfig? = null
        set(value) {
            (field as? Child)?.parent = null
            field = value
            (value as? Child)?.parent = this
        }
    /**
     * Platform that contain logic of resource creation.
     */
    var platform: ResourcesConfig? = null
        set(value) {
            (field as? Child)?.parent = null
            field = value
            (value as? Child)?.parent = this
        }
    /**
     * Define if import should be forced even if this is not necessary.
     * True if import should be forced, false otherwise.
     */
    var isForceImport: Boolean = false
    /**
     * Strategy that should be used for merge conflicts.
     * Default value: REMOVE_PLATFORM.
     * @see [ru.pocketbyte.locolaser.config.Config.ConflictStrategy]
     */
    var conflictStrategy: ConflictStrategy
        get() = conflictStrategyInner ?: ConflictStrategy.KEEP_NEW_PLATFORM
        set(value) { conflictStrategyInner = value }

    private var conflictStrategyInner: ConflictStrategy? = null

    /**
     * Set of locales that should be handled by LocoLaser.
     * You can use [Resources.BASE_LOCALE] to specify base locale.
     */
    var locales: Set<String> = setOf(Resources.BASE_LOCALE)

    /**
     * Define time in minutes that define delay for next localization.
     * Localization will executed not more often the specified delay.
     * If force import switched on delay will be ignored.
     */
    var delay: Long = 0

    /**
     * Define temporary directory.
     */
    var tempDir: File? = null
        get() {
            if (field == null)
                tempDir = platform?.defaultTempDir
            return field
        }

    val extraParams = ExtraParams()

    /**
     * Define if comment should be written even if it equal resource value.
     * True if comment should be written even if it equal resource value, false otherwise.
     */
    var isDuplicateComments: Boolean
        get() = extraParams.duplicateComments
        set(value) {
            extraParams.duplicateComments = value
        }

    /**
     * Define if unsupported quantities should be throw away if is not supported by locale.
     * True if unsupported quantities should be throw away, false otherwise.
     */
    var trimUnsupportedQuantities: Boolean
        get() = extraParams.trimUnsupportedQuantities
        set(value) {
            extraParams.trimUnsupportedQuantities = value
        }
}