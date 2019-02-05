package ru.pocketbyte.locolaser.platform.kotlinmobile.resource.file;

import org.apache.commons.lang3.text.WordUtils;
import ru.pocketbyte.locolaser.config.WritingConfig;
import ru.pocketbyte.locolaser.platform.kotlinmobile.utils.TemplateStr;
import ru.pocketbyte.locolaser.resource.PlatformResources;
import ru.pocketbyte.locolaser.resource.entity.ResItem;
import ru.pocketbyte.locolaser.resource.entity.ResLocale;
import ru.pocketbyte.locolaser.resource.entity.ResMap;
import ru.pocketbyte.locolaser.resource.file.BaseClassResourceFile;

import java.io.File;
import java.io.IOException;

public class KotlinIosResourceFile extends BaseClassResourceFile {

    private static final String CLASS_PACKAGE_TEMPLATE = "package %s\r\n";
    private static final String CLASS_IMPORT_TEMPLATE = "import %s";

    private static final String CLASS_HEADER_TEMPLATE =
            "public class %1$s(private val bundle: NSBundle, private val tableName: String)%2$s {\r\n"
                    + "\r\n"
                    + "    constructor(bundle: NSBundle) : this(bundle, \"Localizable\")\r\n"
                    + "    constructor(tableName: String) : this(NSBundle.mainBundle(), tableName)\r\n"
                    + "    constructor() : this(NSBundle.mainBundle(), \"Localizable\")\r\n";

    private static final String CLASS_FOOTER_TEMPLATE = "}";

    private static final String PROPERTY_TEMPLATE =
            "    %1$spublic val %2$s: String\r\n" +
            "        get() = this.bundle.localizedStringForKey(\"%3$s\", \"\", this.tableName)\r\n";

    private static final String PROPERTY_PLURAL_TEMPLATE =
            "    %1$spublic fun %2$s(count: Int): String {\r\n" +
            "        return NSLocalizedPluralString(\r\n" +
            "            \"%3$s\", this.tableName, this.bundle, count)!!\r\n" +
            "    }\r\n";

    private static final int MAX_LINE_SIZE = 120;

    private String mClassName;
    private String mClassPackage;
    private String mInterfaceName;
    private String mInterfacePackage;

    public KotlinIosResourceFile(File file, String className, String classPackage,
                                     String interfaceName, String interfacePackage) {
        super(file);
        mClassName = className;
        mClassPackage = classPackage;
        mInterfaceName = interfaceName;
        mInterfacePackage = interfacePackage;
    }

    @Override
    public ResMap read() {
        return null;
    }

    @Override
    protected void writeHeaderComment(ResMap resMap, WritingConfig writingConfig) throws IOException {
        writeStringLn(TemplateStr.GENERATED_CLASS_COMMENT);
    }

    @Override
    protected void writeClassHeader(ResMap resMap, WritingConfig writingConfig) throws IOException {
        writeStringLn(String.format(CLASS_PACKAGE_TEMPLATE, mClassPackage));
        writeStringLn(String.format(CLASS_IMPORT_TEMPLATE, "kotlinx.cinterop.*"));
        writeStringLn(String.format(CLASS_IMPORT_TEMPLATE, "platform.Foundation.*"));

        if (isHasPlurals(resMap))
            writeStringLn(String.format(CLASS_IMPORT_TEMPLATE, "localizedPlural.*"));

        if (mInterfaceName != null && mInterfacePackage != null) {
            writeStringLn(String.format(CLASS_IMPORT_TEMPLATE, mInterfacePackage + "." + mInterfaceName));
            writeStringLn("");
            writeStringLn(String.format(CLASS_HEADER_TEMPLATE, mClassName, ": " + mInterfaceName));
        }
        else {
            writeStringLn("");
            writeStringLn(String.format(CLASS_HEADER_TEMPLATE, mClassName, ""));
        }
    }

    @Override
    protected void writeComment(WritingConfig writingConfig, String comment) throws IOException {
        if (mInterfaceName == null || mInterfacePackage == null) {
            String commentLinePrefix = "    * ";
            writeStringLn("    /**");
            writeString(commentLinePrefix);
            writeStringLn(WordUtils.wrap(comment, MAX_LINE_SIZE - commentLinePrefix.length()
                    , "\r\n" + commentLinePrefix, true));
            writeStringLn("    */");
        }
        // Otherwise don't write comments because they already written in interface.
    }

    @Override
    protected void writeProperty(WritingConfig writingConfig, String propertyName, ResItem item) throws IOException {
        boolean isHasInterface = mInterfaceName != null && mInterfacePackage != null;
        writeStringLn(String.format(
                item.isHasQuantities()
                        ? PROPERTY_PLURAL_TEMPLATE
                        : PROPERTY_TEMPLATE,
                isHasInterface ? "override " : "",
                propertyName, item.key.trim()));
    }

    @Override
    protected void writeClassFooter(ResMap resMap, WritingConfig writingConfig) throws IOException {
        writeString(CLASS_FOOTER_TEMPLATE);
    }

    private boolean isHasPlurals(ResMap resMap) {
        ResLocale locale = resMap.get(PlatformResources.BASE_LOCALE);
        if (locale != null) {
            for (ResItem item: locale.values()) {
                if (item.isHasQuantities())
                    return true;
            }
        }
        return false;
    }
}
