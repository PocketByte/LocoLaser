package ru.pocketbyte.locolaser.platform.kotlinmobile.resource.file

import org.apache.commons.lang3.text.WordUtils
import ru.pocketbyte.locolaser.config.WritingConfig
import ru.pocketbyte.locolaser.platform.kotlinmobile.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.PlatformResources
import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.resource.entity.ResLocale
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.file.BaseClassResourceFile

import java.io.File
import java.io.IOException

class KotlinIosResourceFile(
        file: File,
        private val mClassName: String,
        private val mClassPackage: String,
        private val mInterfaceName: String?,
        private val mInterfacePackage: String?
) : BaseClassResourceFile(file) {

    companion object {

        private const val CLASS_PACKAGE_TEMPLATE = "package %s\r\n"
        private const val CLASS_IMPORT_TEMPLATE = "import %s"

        private const val CLASS_HEADER_TEMPLATE =
                "public class %1\$s(private val bundle: NSBundle, private val tableName: String)%2\$s {\r\n" +
                        "\r\n" +
                        "    constructor(bundle: NSBundle) : this(bundle, \"Localizable\")\r\n" +
                        "    constructor(tableName: String) : this(NSBundle.mainBundle(), tableName)\r\n" +
                        "    constructor() : this(NSBundle.mainBundle(), \"Localizable\")\r\n"

        private const val CLASS_FOOTER_TEMPLATE = "}"

        private const val PROPERTY_TEMPLATE =
                "    %1\$spublic val %2\$s: String\r\n" +
                        "        get() = this.bundle.localizedStringForKey(\"%3\$s\", \"\", this.tableName)\r\n"

        private const val PROPERTY_PLURAL_TEMPLATE =
                "    %1\$spublic fun %2\$s(count: Int): String {\r\n" +
                        "        return NSLocalizedPluralString(\r\n" +
                        "            \"%3\$s\", this.tableName, this.bundle, count)!!\r\n" +
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
        writeStringLn(String.format(CLASS_PACKAGE_TEMPLATE, mClassPackage))
        writeStringLn(String.format(CLASS_IMPORT_TEMPLATE, "kotlinx.cinterop.*"))
        writeStringLn(String.format(CLASS_IMPORT_TEMPLATE, "platform.Foundation.*"))

        if (isHasPlurals(resMap))
            writeStringLn(String.format(CLASS_IMPORT_TEMPLATE, "localizedPlural.*"))

        if (mInterfaceName != null && mInterfacePackage != null) {
            writeStringLn(String.format(CLASS_IMPORT_TEMPLATE, "$mInterfacePackage.$mInterfaceName"))
            writeStringLn("")
            writeStringLn(String.format(CLASS_HEADER_TEMPLATE, mClassName, ": $mInterfaceName"))
        } else {
            writeStringLn("")
            writeStringLn(String.format(CLASS_HEADER_TEMPLATE, mClassName, ""))
        }
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

    private fun isHasPlurals(resMap: ResMap): Boolean {
        val locale = resMap[PlatformResources.BASE_LOCALE]
        if (locale != null) {
            for (item in locale.values) {
                if (item.isHasQuantities)
                    return true
            }
        }
        return false
    }
}
