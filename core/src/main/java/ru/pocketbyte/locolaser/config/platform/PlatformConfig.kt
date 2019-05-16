/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.platform

import ru.pocketbyte.locolaser.resource.PlatformResources

import java.io.File

/**
 * Configuration object that contains information about localization rules for specified platform.
 *
 * @author Denis Shurygin
 */
interface PlatformConfig {

    /**
     * Gets name of the platform.
     * @return Name of the platform.
     */
    val type: String

    // TODO docs
    val resources: PlatformResources

    /**
     * Gets temporary directory.
     * @return Temporary directory.
     */
    val defaultTempDir: File
}
