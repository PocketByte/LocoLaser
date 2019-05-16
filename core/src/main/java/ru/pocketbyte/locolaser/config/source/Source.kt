/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.source

import ru.pocketbyte.locolaser.resource.entity.Quantity
import ru.pocketbyte.locolaser.resource.entity.ResMap

/**
 *
 * @author Denis Shurygin
 */
abstract class Source(open val sourceConfig: SourceConfig) {

    abstract val modifiedDate: Long

    abstract fun read(): ReadResult
    abstract fun write(resMap: ResMap)

    abstract fun close()

    class ReadResult(val items: ResMap?, val missedValues: List<MissedValue>?)

    class MissedValue(val key: String, val locale: String, val quantity: Quantity, val location: ValueLocation)

    abstract class ValueLocation(val source: Source)
}