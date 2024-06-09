/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.resources

import ru.pocketbyte.locolaser.resource.Resources
import java.io.Serializable

/**
 * Configuration object that contains information about localization rules for specified platform.
 *
 * @author Denis Shurygin
 */
interface ResourcesConfig : Serializable {

    /**
     * Gets name of the platform.
     * @return Name of the platform.
     */
    val type: String

    /**
     * Gets resources of the platform.
     */
    val resources: Resources
}
