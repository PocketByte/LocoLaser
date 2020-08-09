/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.source

import ru.pocketbyte.locolaser.resource.PlatformResources
import ru.pocketbyte.locolaser.summary.FileSummary

/**
 *
 * @author Denis Shurygin
 */
abstract class Source(): PlatformResources {

    abstract val modifiedDate: Long

    abstract fun close()

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(0, null)
    }
}