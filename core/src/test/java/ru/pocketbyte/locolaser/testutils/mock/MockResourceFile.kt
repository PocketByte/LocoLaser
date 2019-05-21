package ru.pocketbyte.locolaser.testutils.mock

import ru.pocketbyte.locolaser.config.WritingConfig
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.file.ResourceFile

import java.io.IOException

class MockResourceFile(
        var map: ResMap?
) : ResourceFile {

    override fun read(): ResMap? {
        return map
    }

    @Throws(IOException::class)
    override fun write(resMap: ResMap, writingConfig: WritingConfig?) {
        map = resMap
    }
}
