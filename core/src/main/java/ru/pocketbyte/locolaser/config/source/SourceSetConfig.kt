/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.source

import ru.pocketbyte.locolaser.utils.LogUtils

import java.util.HashSet

/**
 * @author Denis Shurygin
 */
class SourceSetConfig(private val mConfigs: Set<SourceConfig>, private val mDefaultSourceConfig: SourceConfig?) : SourceConfig {

    override val type: String = "set"
    override val locales: Set<String> = mConfigs.flatMap { it.locales }.toSet()

    override fun open(): SourceSet? {
        var defaultSource: Source? = null
        val sources = HashSet<Source>(mConfigs.size)
        for (config in mConfigs) {
            val source = config.open()
            if (source == null) {
                LogUtils.err("Failed to open source. Source: " + config.toString())
                for (sourceToClose in sources)
                    sourceToClose.close()
                return null
            }
            sources.add(source)

            if (config === mDefaultSourceConfig)
                defaultSource = source
        }
        return SourceSet(this, sources, defaultSource!!)
    }
}
