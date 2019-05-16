package ru.pocketbyte.locolaser.resource.file

import ru.pocketbyte.locolaser.config.WritingConfig
import ru.pocketbyte.locolaser.resource.PlatformResources
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.utils.TextUtils

import java.io.File
import java.io.IOException
import java.util.HashSet

abstract class BaseClassResourceFile(file: File) : ResourceStreamFile(file) {

    @Throws(IOException::class)
    protected abstract fun writeHeaderComment(resMap: ResMap, writingConfig: WritingConfig?)

    @Throws(IOException::class)
    protected abstract fun writeClassHeader(resMap: ResMap, writingConfig: WritingConfig?)

    @Throws(IOException::class)
    protected abstract fun writeComment(writingConfig: WritingConfig?, comment: String)

    @Throws(IOException::class)
    protected abstract fun writeProperty(writingConfig: WritingConfig?, propertyName: String, item: ResItem)

    @Throws(IOException::class)
    protected abstract fun writeClassFooter(resMap: ResMap, writingConfig: WritingConfig?)

    override fun read(): ResMap? {
        return null
    }

    @Throws(IOException::class)
    override fun write(resMap: ResMap, writingConfig: WritingConfig?) {
        val locale = resMap[PlatformResources.BASE_LOCALE]
        if (locale != null) {
            open()
            writeHeaderComment(resMap, writingConfig)
            writeln()
            writeClassHeader(resMap, writingConfig)

            val keysSet = HashSet<String>()
            for (item in locale.values) {
                val propertyName = TextUtils.keyToProperty(item.key)
                if (!keysSet.contains(propertyName)) {

                    keysSet.add(propertyName)

                    val valueOther = item.valueForQuantity(Quantity.OTHER)
                    if (valueOther?.value != null) {
                        writeComment(writingConfig, valueOther.value)
                    }

                    writeProperty(writingConfig, propertyName, item)
                }
            }

            writeClassFooter(resMap, writingConfig)
            close()
        }
    }
}
