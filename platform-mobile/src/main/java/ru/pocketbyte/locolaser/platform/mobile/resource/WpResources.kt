/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource

import ru.pocketbyte.locolaser.resource.AbsPlatformResources
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.File

/**
 * @author Denis Shurygin
 */
class WpResources(resourcesDir: File, fileName: String) : AbsPlatformResources(resourcesDir, fileName) {

    override fun getResourceFiles(locales: Set<String>): Array<ResourceFile>? {
        return null
    }

    override fun summaryForLocale(locale: String): FileSummary? {
        return null
    }

}
