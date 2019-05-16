/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.source

import ru.pocketbyte.locolaser.config.Config

/**
 * Base implementation of SourceConfig.
 *
 * @author Denis Shurygin
 */
abstract class BaseTableSourceConfig : Config.Child(), SourceConfig {

    /**
     * Title of the column that contain resource Key's.
     */
    var keyColumn: String? = null

    /**
     * Title of the column that contain resource quantity.
     */
    var quantityColumn: String? = null

    /**
     * Titles set of the columns that contain resource values.
     */
    var localeColumns: Set<String>? = null

    /**
     * Title of the column that contain comments.
     */
    var commentColumn: String? = null

    override val locales: Set<String>
        get() = localeColumns ?: emptySet()

    override fun toString(): String {
        return type
    }
}
