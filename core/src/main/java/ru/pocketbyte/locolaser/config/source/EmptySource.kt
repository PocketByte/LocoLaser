package ru.pocketbyte.locolaser.config.source

import ru.pocketbyte.locolaser.resource.entity.ResMap

class EmptySource(sourceConfig: SourceConfig) : Source(sourceConfig) {

    override val modifiedDate: Long = 0

    override fun read(): ReadResult {
        return ReadResult(ResMap(), null)
    }

    override fun write(resMap: ResMap) {
        // do nothing
    }

    override fun close() {
        // do nothing
    }
}