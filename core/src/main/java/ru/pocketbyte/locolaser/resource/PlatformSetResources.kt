package ru.pocketbyte.locolaser.resource

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.entity.merge
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.IOException

class PlatformSetResources(
        private val resources: Set<PlatformResources>
) : PlatformResources {

    override fun read(locales: Set<String>, extraParams: ExtraParams): ResMap {
        return ResMap().apply {
            resources.forEach {
                merge(it.read(locales, extraParams))
            }
        }
    }

    @Throws(IOException::class)
    override fun write(map: ResMap, extraParams: ExtraParams?) {
        resources.forEach {
            it.write(map, extraParams)
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
