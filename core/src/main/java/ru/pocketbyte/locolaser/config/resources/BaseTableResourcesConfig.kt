/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.resources

import ru.pocketbyte.locolaser.config.Config

/**
 * Base implementation of SourceConfig.
 *
 * @author Denis Shurygin
 */
abstract class BaseTableResourcesConfig(
    /**
     * Title of the column that contain resource Key's.
     */
    val keyColumn: String,

    /**
     * Title of the column that contain resource quantity.
     */
    val quantityColumn: String?,

    /**
     * Title of the column that contain comments.
     */
    val commentColumn: String?,

    /**
     * Title of the column that contain meta data quantity.
     */
    val metadataColumn: String?
) : Config.Child(), ResourcesConfig {

    override fun toString(): String {
        return type
    }
}
