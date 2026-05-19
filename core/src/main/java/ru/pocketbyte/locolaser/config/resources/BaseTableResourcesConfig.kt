/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.resources

/**
 * Base implementation of [ResourcesConfig] for table-based resource sources (e.g., spreadsheets).
 *
 * @author Denis Shurygin
 */
abstract class BaseTableResourcesConfig(
    /**
     * Title of the column that contains resource keys.
     */
    val keyColumn: String,

    /**
     * Title of the column that contains resource quantity.
     */
    val quantityColumn: String?,

    /**
     * Title of the column that contains comments.
     */
    val commentColumn: String?,

    /**
     * Title of the column that contains metadata.
     */
    val metadataColumn: String?
) : ResourcesConfig {

    override fun toString(): String {
        return type
    }
}
