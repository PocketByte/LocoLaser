/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.source

import ru.pocketbyte.locolaser.resource.entity.ResMap

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

    override fun read(): ReadResult {
        var missedValues: MutableList<MissedValue>? = null
        var resMap: ResMap? = null
        for (source in mSources) {
            val result = source.read()
            if (result.missedValues != null) {
                if (missedValues == null)
                    missedValues = ArrayList()
                missedValues.addAll(result.missedValues)
            }
            resMap = resMap?.merge(result.items) ?: ResMap(result.items)
        }
        return ReadResult(resMap, missedValues)
    }

    override fun write(resMap: ResMap) {
        mDefaultSource.write(resMap)
    }

    override fun close() {
        for (source in mSources)
            source.close()
    }
}
