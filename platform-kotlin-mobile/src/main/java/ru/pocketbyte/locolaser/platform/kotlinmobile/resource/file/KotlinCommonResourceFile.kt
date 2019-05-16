package ru.pocketbyte.locolaser.platform.kotlinmobile.resource.file

import org.apache.commons.lang3.text.WordUtils
import ru.pocketbyte.locolaser.config.WritingConfig
import ru.pocketbyte.locolaser.platform.kotlinmobile.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.file.BaseClassResourceFile

import java.io.File
import java.io.IOException

class KotlinCommonResourceFile(
        file: File,
        private val mClassName: String,
        private val mClassPackage: String
) : BaseClassResourceFile(file) {

    companion object {

        private const val CLASS_HEADER_TEMPLATE =
                "package %1\$s\r\n" +
                        "\r\n" +
                        "interface %2\$s {\r\n"

        private const val CLASS_FOOTER_TEMPLATE = "}"

        private const val PROPERTY_TEMPLATE = "    val %s: String\r\n"
        private const val PROPERTY_PLURAL_TEMPLATE = "    fun %s(count: Int): String\r\n"

        private const val MAX_LINE_SIZE = 120
    }

    override fun read(): ResMap? {
        return null
    }

    @Throws(IOException::class)
    override fun writeHeaderComment(resMap: ResMap, writingConfig: WritingConfig?) {
        writeStringLn(TemplateStr.GENERATED_CLASS_COMMENT)
    }

    @Throws(IOException::class)
    override fun writeClassHeader(resMap: ResMap, writingConfig: WritingConfig?) {
        writeStringLn(String.format(CLASS_HEADER_TEMPLATE, mClassPackage, mClassName))
    }

    @Throws(IOException::class)
    override fun writeComment(writingConfig: WritingConfig?, comment: String) {
        val commentLinePrefix = "    * "
        writeStringLn("    /**")
        writeString(commentLinePrefix)
        writeStringLn(WordUtils.wrap(comment, MAX_LINE_SIZE - commentLinePrefix.length, "\r\n" + commentLinePrefix, true))
        writeStringLn("    */")
    }

    @Throws(IOException::class)
    override fun writeProperty(writingConfig: WritingConfig?, propertyName: String, item: ResItem) {
        writeStringLn(String.format(
                if (item.isHasQuantities)
                    PROPERTY_PLURAL_TEMPLATE
                else
                    PROPERTY_TEMPLATE,
                propertyName))
    }

    @Throws(IOException::class)
    override fun writeClassFooter(resMap: ResMap, writingConfig: WritingConfig?) {
        writeString(CLASS_FOOTER_TEMPLATE)
    }
}
