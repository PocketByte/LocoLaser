/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.resources

import ru.pocketbyte.locolaser.config.Config
import java.io.File
import java.io.IOException
import java.util.regex.Pattern

/**
 * Base implementation of PlatformConfig.
 *
 * @author Denis Shurygin
 */
/**
 * Construct new Platform object.
 */
abstract class BaseResourcesConfig : Config.Child(), ResourcesConfig {

    companion object {
        fun regExFilter(filter: String?): ((key: String) -> Boolean)? {
            if (filter == null)
                return null

            val matcher = Pattern.compile(filter).matcher("")
            return {
                matcher.reset(it).find()
            }
        }
    }

    /**
     * Resource name or null if should be used default name.
     */
    var resourceName: String? = null
        get() = if (field != null) field else defaultResourceName

    /**
     * Resource directory.
     */
    var resourcesDir: File? = null
        get() {
            if (field == null) {
                resourcesDir = try {
                    File(File(defaultResourcesPath).canonicalPath)
                } catch (e: IOException) {
                    e.printStackTrace()
                    File(defaultResourcesPath)
                }

            }
            return field
        }

    var filter: ((key: String) -> Boolean)? = null

    // =================================================================================================================
    // ========= Interface properties =====================================================================================
    // =================================================================================================================

    private var _defaultTempDir: File? = null
    override val defaultTempDir: File
        get() {
            if (_defaultTempDir == null)
                _defaultTempDir = try {
                    File(File(defaultTempDirPath).canonicalPath)
                } catch (e: IOException) {
                    e.printStackTrace()
                    File(defaultTempDirPath)
                }

            return _defaultTempDir!!
        }

    // =================================================================================================================
    // ========= Abstract properties ======================================================================================
    // =================================================================================================================

    /**
     * Default temporary directory path specified for current platform.
     */
    abstract val defaultTempDirPath: String

    /**
     * Default resource directory path specified for current platform.
     */
    abstract val defaultResourcesPath: String

    /**
     * Default resource name specified for current platform.
     */
    protected abstract val defaultResourceName: String
}