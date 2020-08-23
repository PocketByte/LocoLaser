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
abstract class BaseTableResourcesConfig : Config.Child(), ResourcesConfig {

    /**
     * Title of the column that contain resource Key's.
     */
    var keyColumn: String? = null

    /**
     * Title of the column that contain resource quantity.
     */
    var quantityColumn: String? = null

    /**
     * Title of the column that contain comments.
     */
    var commentColumn: String? = null

    /**
     * Title of the column that contain meta data quantity.
     */
    var metadataColumn: String? = null

    override fun toString(): String {
        return type
    }
}
