/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.resources

import ru.pocketbyte.locolaser.resource.Resources

/**
 * Configuration object that contains information about localization rules for specified platform.
 *
 * @author Denis Shurygin
 */
interface ResourcesConfig {

    /**
     * Gets name of the platform.
     * @return Name of the platform.
     */
    val type: String

    // TODO docs
    val resources: Resources

    /**
     * Default temporary directory path specified for current resource and platform.
     */
    val defaultTempDirPath: String
}
