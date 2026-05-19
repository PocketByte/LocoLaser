/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.resources

import ru.pocketbyte.locolaser.resource.Resources
import java.io.Serializable

/**
 * Defines the configuration for a resource source or target platform.
 *
 * @author Denis Shurygin
 */
interface ResourcesConfig : Serializable {

    /**
     * The type identifier of this resource configuration.
     */
    val type: String

    /**
     * The [Resources] instance associated with this configuration.
     */
    val resources: Resources
}
