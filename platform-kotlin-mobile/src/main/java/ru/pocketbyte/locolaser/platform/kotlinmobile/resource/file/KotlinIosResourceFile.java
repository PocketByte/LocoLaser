package ru.pocketbyte.locolaser.platform.kotlinmobile.resource.file;

import org.apache.commons.lang3.text.WordUtils;
import ru.pocketbyte.locolaser.platform.kotlinmobile.utils.TemplateStr;
import ru.pocketbyte.locolaser.resource.entity.ResItem;
import ru.pocketbyte.locolaser.resource.entity.ResMap;
import ru.pocketbyte.locolaser.resource.file.BaseClassResourceFile;

import java.io.File;
import java.io.IOException;

public class KotlinIosResourceFile extends BaseClassResourceFile {

    private static final String CLASS_HEADER_TEMPLATE = "package %1$s\r\n"
            + "\r\n"
            + "import kotlinx.cinterop.*\r\n"
            + "import platform.Foundation.*\r\n"
            + "\r\n"
            + "public class %2$s(private val bundle: Bundle, private val tableName: String) {\r\n"
            + "\r\n"
            + "    constructor(bundle: NSBundle) : this(bundle, \"Localizable\")\r\n"
            + "    constructor(tableName: String) : this(NSBundle.mainBundle(), tableName)\r\n"
            + "    constructor() : this(NSBundle.mainBundle(), \"Localizable\")\r\n";

    private static final String CLASS_HEADER_TEMPLATE_WITH_INTERFACE = "package %1$s\r\n"
            + "\r\n"
            + "import kotlinx.cinterop.*\r\n"
            + "import platform.Foundation.*\r\n"
            + "import %3$s.%4$s\r\n"
            + "\r\n"
            + "public class %2$s(private val bundle: Bundle, private val tableName: String): %4$s {\r\n"
            + "\r\n"
            + "    constructor(bundle: NSBundle) : this(bundle, \"Localizable\")\r\n"
            + "    constructor(tableName: String) : this(NSBundle.mainBundle(), tableName)\r\n"
            + "    constructor() : this(NSBundle.mainBundle(), \"Localizable\")\r\n";

    private static final String CLASS_FOOTER_TEMPLATE = "}";

    private static final String PROPERTY_TEMPLATE =
            "    public val %1$s: String\r\n" +
            "        get() = this.bundle.localizedStringForKey(\"%2$s\", \"\", this.tableName)\r\n";
    private static final String PROPERTY_TEMPLATE_WITH_OVERRIDE =
            "    override public val %1$s: String\r\n" +
            "        get() = this.bundle.localizedStringForKey(\"%2$s\", \"\", this.tableName)\r\n";

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
    protected void writeHeaderComment() throws IOException {
        writeStringLn(TemplateStr.GENERATED_CLASS_COMMENT);
    }

    @Override
    protected void writeClassHeader() throws IOException {
        if (mInterfaceName != null && mInterfacePackage != null)
            writeStringLn(String.format(CLASS_HEADER_TEMPLATE_WITH_INTERFACE,
                    mClassPackage, mClassName,
                    mInterfacePackage, mInterfaceName));
        else
            writeStringLn(String.format(CLASS_HEADER_TEMPLATE,
                    mClassPackage, mClassName));
    }

    @Override
    protected void writeComment(String comment) throws IOException {
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
    protected void writeProperty(String propertyName, ResItem item) throws IOException {
        String template = (mInterfaceName != null && mInterfacePackage != null)
                ? PROPERTY_TEMPLATE_WITH_OVERRIDE
                : PROPERTY_TEMPLATE;

        writeStringLn(String.format(template,
                propertyName, item.key.trim()));
    }

    @Override
    protected void writeClassFooter() throws IOException {
        writeString(CLASS_FOOTER_TEMPLATE);
    }

}
