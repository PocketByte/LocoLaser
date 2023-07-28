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

    private var _resourceName: String? = null

    /**
     * Resource name or null if should be used default name.
     */
    var resourceName: String
        get() = _resourceName ?: defaultResourceName
        set(value) { _resourceName = value }

    private var _resourcesDir: File? = null

    /**
     * Resource directory.
     */
    var resourcesDir: File
        get() {
            return _resourcesDir ?: try {
                File(File(defaultResourcesPath).canonicalPath)
            } catch (e: IOException) {
                e.printStackTrace()
                File(defaultResourcesPath)
            }.apply {
                _resourcesDir = this
            }
        }
        set(value) { _resourcesDir = value }

    /**
     * ResourceFileProvider provides resource File depending on locale, directory and name.
     */
    abstract var resourceFileProvider: ResourceFileProvider

    var filter: ((key: String) -> Boolean)? = null

    // =================================================================================================================
    // ========= Interface properties =====================================================================================
    // =================================================================================================================

    override val defaultTempDir: File by lazy {
        try {
            File(File(defaultTempDirPath).canonicalPath)
        } catch (e: IOException) {
            e.printStackTrace()
            File(defaultTempDirPath)
        }
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