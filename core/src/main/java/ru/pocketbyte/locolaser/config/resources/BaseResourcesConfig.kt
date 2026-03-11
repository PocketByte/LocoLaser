/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.resources

import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import ru.pocketbyte.locolaser.utils.buildFileFrom
import java.io.File

/**
 * Base implementation of PlatformConfig.
 *
 * @author Denis Shurygin
 */
/**
 * Construct new Platform object.
 */
abstract class BaseResourcesConfig(
    private val workDir: File?,
    /**
     * Resource name or null if should be used default name.
     */
    private val resName: String?,

    /**
     * Resource directory path.
     */
    val resourcesDirPath: String?,

    /**
     * ResourceFileProvider provides resource File depending on locale, directory and name.
     */
    val resourceFileProvider: ResourceFileProvider,

    val filter: ResourcesFilter?
) : ResourcesConfig {

    /**
     * Resource name.
     */
    val resourceName: String
        get() = resName ?: defaultResourceName

    /**
     * Resource directory.
     */
    val resourcesDir: File
        get() = buildFileFrom(workDir, resourcesDirPath ?: defaultResourcesPath)

    // =================================================================================================================
    // ========= Abstract properties ======================================================================================
    // =================================================================================================================



    /**
     * Default resource directory path specified for current platform.
     */
    protected abstract val defaultResourcesPath: String

    /**
     * Default resource name specified for current platform.
     */
    protected abstract val defaultResourceName: String
}