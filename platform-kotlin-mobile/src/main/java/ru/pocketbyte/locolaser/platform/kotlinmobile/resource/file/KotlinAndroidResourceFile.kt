package ru.pocketbyte.locolaser.platform.kotlinmobile.resource.file

import org.apache.commons.lang3.text.WordUtils
import ru.pocketbyte.locolaser.config.WritingConfig
import ru.pocketbyte.locolaser.platform.kotlinmobile.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.file.BaseClassResourceFile

import java.io.File
import java.io.IOException

class KotlinAndroidResourceFile(
        file: File,
        private val mClassName: String,
        private val mClassPackage: String,
        private val mInterfaceName: String?,
        private val mInterfacePackage: String?,
        private val mAppPackage: String
) : BaseClassResourceFile(file) {

    companion object {

        private const val CLASS_HEADER_TEMPLATE =
                "package %1\$s\r\n" +
                "\r\n" +
                "import android.content.Context\r\n" +
                "import %3\$s.R\r\n" +
                "\r\n" +
                "public class %2\$s(private val context: Context) {\r\n"

        private const val CLASS_HEADER_TEMPLATE_WITH_INTERFACE =
                "package %1\$s\r\n" +
                "\r\n" +
                "import android.content.Context\r\n" +
                "import %3\$s.R\r\n" +
                "import %4\$s.%5\$s\r\n" +
                "\r\n" +
                "public class %2\$s(private val context: Context): %5\$s {\r\n"

        private const val CLASS_FOOTER_TEMPLATE = "}"

        private const val PROPERTY_TEMPLATE =
                "    %1\$spublic val %2\$s: String\r\n" +
                "        get() = this.context.getString(R.string.%3\$s)\r\n"

        private const val PROPERTY_PLURAL_TEMPLATE =
                "    %1\$spublic fun %2\$s(count: Int): String {\r\n" +
                "        return this.context.getString(R.plurals.%3\$s, count)\r\n" +
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
                    mClassPackage, mClassName, mAppPackage,
                    mInterfacePackage, mInterfaceName))
        else
            writeStringLn(String.format(CLASS_HEADER_TEMPLATE,
                    mClassPackage, mClassName, mAppPackage))
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