package ru.pocketbyte.locolaser.testutils.mock

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType

import java.io.IOException

class MockResourceFile(
        var map: ResMap?
) : ResourceFile {

    override val formattingType = NoFormattingType

    override fun read(extraParams: ExtraParams?): ResMap? {
        return map
    }

    @Throws(IOException::class)
    override fun write(resMap: ResMap, extraParams: ExtraParams?) {
        map = resMap
    }
}
