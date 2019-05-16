package ru.pocketbyte.locolaser.resource

import ru.pocketbyte.locolaser.config.WritingConfig
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.IOException

class PlatformSetResources(
        private val resources: Set<PlatformResources>
) : PlatformResources {

    override fun read(locales: Set<String>): ResMap {
        return ResMap().apply {
            resources.forEach {
                merge(it.read(locales))
            }
        }
    }

    @Throws(IOException::class)
    override fun write(map: ResMap, writingConfig: WritingConfig?) {
        resources.forEach {
            it.write(map, writingConfig)
        }
    }

    override fun summaryForLocale(locale: String): FileSummary {
        var bytes: Long = 0
        val hash = StringBuilder()

        resources.map { it.summaryForLocale(locale) }.forEach {
            bytes += it.bytes
            hash.append(it.hash)
        }

        return FileSummary(bytes, hash.toString())
    }
}
