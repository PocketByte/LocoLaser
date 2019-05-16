/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config

import ru.pocketbyte.locolaser.config.platform.PlatformConfig
import ru.pocketbyte.locolaser.config.source.SourceConfig

import java.io.File
import java.io.IOException

/**
 * Configuration object that contain information about localization rules.
 *
 * @author Denis Shurygin
 */
// =================================================================================================================
// ============ Constructors =======================================================================================
// =================================================================================================================

/**
 * Construct new configuration object.
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

        /** New platform resources should be exported into source if source doesn't contain this resources.  */
        EXPORT_NEW_PLATFORM("export_new_platform");

        override fun toString(): String {
            return strValue
        }
    }

    // =================================================================================================================
    // ============ Getters ============================================================================================
    // =================================================================================================================

    /**
     * Gets file from which this config was read.
     * @return File from which this config was read.
     */
    // =================================================================================================================
    // ============ Public methods =====================================================================================
    // =================================================================================================================

    // =================================================================================================================
    // ============ Setters ============================================================================================
    // =================================================================================================================

    /**
     * Sets file from which this config was read.
     * @param file File from which this config was read.
     */
    var file: File? = null
    /**
     * Gets source that contain resources.
     * @return Source that contain resources.
     */
    /**
     * Sets source that contain resources.
     * @param sourceConfig Source that contain resources.
     */
    var sourceConfig: SourceConfig? = null
        set(sourceConfig) {
            if (this.sourceConfig is Child)
                (this.sourceConfig as Child).parent = null

            field = sourceConfig

            if (this.sourceConfig is Child)
                (this.sourceConfig as Child).parent = this
        }
    /**
     * Gets platform that contain logic of resource creation.
     * @return Platform that contain logic of resource creation.
     */
    /**
     * Sets platform that contain logic of resource creation.
     * @param platform Platform that contain logic of resource creation.
     */
    var platform: PlatformConfig? = null
        set(platform) {
            if (this.platform is Child)
                (this.platform as Child).parent = null

            field = platform

            if (this.platform is Child)
                (this.platform as Child).parent = this
        }
    /**
     * Gets if import should be forced even if this is not necessary.
     * @return True if import should be forced, false otherwise.
     */
    /**
     * Sets if import should be forced even if this is not necessary. Default value: false.
     * @param isForceImport True if import should be forced, false otherwise.
     */
    var isForceImport: Boolean = false
    /**
     * Gets which strategy should be processed for conflicts.
     * @return Strategy which should be processed for conflicts.
     */
    /**
     * Sets which strategy should be processed for conflicts. Default value: REMOVE_PLATFORM.
     * @param strategy see [ru.pocketbyte.locolaser.config.Config.ConflictStrategy]
     */
    var conflictStrategy: ConflictStrategy? = null
        get() = if (field == null) ConflictStrategy.KEEP_NEW_PLATFORM else field
    /**
     * Gets time in milliseconds that define delay for next localization. Localization will executed not more often the specified delay. If force import switch on delay will be ignored.
     * @return Time in milliseconds that define delay for next localization.
     */
    /**
     * Sets time in milliseconds that define delay for next localization. Localization will executed not more often the specified delay. If force import switch on delay will be ignored.
     * @param delay Time in milliseconds that define delay for next localization.
     */
    var delay: Long = 0
    /**
     * Sets temporary directory.
     * @param file Temporary directory.
     */
    var tempDir: File? = null
        get() {
            if (field == null)
                tempDir = platform?.defaultTempDir
            return field
        }

    val writingConfig = WritingConfig()

    /**
     * Gets if comment should be written even if it equal resource value.
     * @return True if comment should be written even if it equal resource value, false otherwise.
     */
    /**
     * Sets if comment should be written even if it equal resource value. Default value: false.
     * @param isDuplicateComments True if comment should be written even if it equal resource value, false otherwise.
     */
    var isDuplicateComments: Boolean
        get() = writingConfig.isDuplicateComments
        set(isDuplicateComments) {
            writingConfig.isDuplicateComments = isDuplicateComments
        }
}