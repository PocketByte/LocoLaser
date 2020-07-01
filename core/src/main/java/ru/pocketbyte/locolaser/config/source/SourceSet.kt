/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.source

import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.entity.merge

import java.util.ArrayList

/**
 * @author Denis Shurygin
 */
class SourceSet(
        sourceConfig: SourceConfig,
        private val mSources: Set<Source>,
        private val mDefaultSource: Source
) : Source(sourceConfig) {

    override val modifiedDate: Long
        get() = mSources
                .map { it.modifiedDate }
                .max() ?: 0

    override fun read(): ResMap? {
        var resMap: ResMap? = null
        for (source in mSources) {
            resMap = resMap.merge(source.read())
        }
        return resMap
    }

    override fun write(resMap: ResMap) {
        mDefaultSource.write(resMap)
    }

    override fun close() {
        for (source in mSources)
            source.close()
    }
}
