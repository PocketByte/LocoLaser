package ru.pocketbyte.locolaser.platform.kotlinmpp.resource.file

import org.apache.commons.lang3.text.WordUtils
import ru.pocketbyte.locolaser.config.WritingConfig
import ru.pocketbyte.locolaser.platform.kotlinmpp.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.file.BaseClassResourceFile

import java.io.File
import java.io.IOException

class KotlinJsResourceFile(
        file: File,
        private val mClassName: String,
        private val mClassPackage: String,
        private val mInterfaceName: String?,
        private val mInterfacePackage: String?
) : BaseClassResourceFile(file) {

    companion object {

        private const val CLASS_HEADER_TEMPLATE =
                "package %1\$s\r\n" +
                        "\r\n" +
                        "import i18next.I18n\r\n" +
                        "\r\n" +
                        "public class %2\$s(private val i18n: I18n) {\r\n" +
                        "\r\n" +
                        "    data class Plural(val count: Int)\r\n"

        private const val CLASS_HEADER_TEMPLATE_WITH_INTERFACE =
                "package %1\$s\r\n" +
                        "\r\n" +
                        "import i18next.I18n\r\n" +
                        "import %3\$s.%4\$s\r\n" +
                        "\r\n" +
                        "public class %2\$s(private val i18n: I18n): %4\$s {\r\n" +
                        "\r\n" +
                        "    data class Plural(val count: Int)\r\n"

        private const val CLASS_FOOTER_TEMPLATE =
                        "}"

        private const val PROPERTY_TEMPLATE =
                "    public %1\$sval %2\$s: String\r\n" +
                        "        get() = this.i18n.t(\"%3\$s\")\r\n"

        private const val PROPERTY_PLURAL_TEMPLATE =
                "    public %1\$sfun %2\$s(count: Int): String {\r\n" +
                        "        return this.i18n.t(\"%3\$s_plural\", Plural(count))\r\n" +
                        "    }\r\n"

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
        if (mInterfaceName != null && mInterfacePackage != null)
            writeStringLn(String.format(CLASS_HEADER_TEMPLATE_WITH_INTERFACE,
                    mClassPackage, mClassName,
                    mInterfacePackage, mInterfaceName))
        else
            writeStringLn(String.format(CLASS_HEADER_TEMPLATE,
                    mClassPackage, mClassName))
    }

    @Throws(IOException::class)
    override fun writeComment(writingConfig: WritingConfig?, comment: String) {
        if (mInterfaceName == null || mInterfacePackage == null) {
            val commentLinePrefix = "    * "
            writeStringLn("    /**")
            writeString(commentLinePrefix)
            writeStringLn(WordUtils.wrap(comment, MAX_LINE_SIZE - commentLinePrefix.length, "\r\n" + commentLinePrefix, true))
            writeStringLn("    */")
        }
        // Otherwise don't write comments because they already written in interface.
    }

    @Throws(IOException::class)
    override fun writeProperty(writingConfig: WritingConfig?, propertyName: String, item: ResItem) {
        val isHasInterface = mInterfaceName != null && mInterfacePackage != null
        writeStringLn(String.format(
                if (item.isHasQuantities)
                    PROPERTY_PLURAL_TEMPLATE
                else
                    PROPERTY_TEMPLATE,
                if (isHasInterface) "override " else "",
                propertyName, item.key.trim { it <= ' ' }))
    }

    @Throws(IOException::class)
    override fun writeClassFooter(resMap: ResMap, writingConfig: WritingConfig?) {
        writeString(CLASS_FOOTER_TEMPLATE)
    }
}