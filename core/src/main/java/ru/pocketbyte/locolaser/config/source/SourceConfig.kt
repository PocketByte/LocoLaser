/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.source

/**
 * Configuration object that contains information about source.
 *
 * @author Denis Shurygin
 */
interface SourceConfig {

    val type: String

    /**
     * Open source that represents by this configuration.
     * @return Source that represents by this configuration or null if configuration is invalid.
     */
    fun open(): Source?

    /**
     * List of the locales that contain in the source.
     */
    val locales: Set<String>

}
