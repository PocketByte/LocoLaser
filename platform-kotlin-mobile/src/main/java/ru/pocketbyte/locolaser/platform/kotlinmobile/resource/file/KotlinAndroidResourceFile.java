package ru.pocketbyte.locolaser.platform.kotlinmobile.resource.file;

import org.apache.commons.lang3.text.WordUtils;
import ru.pocketbyte.locolaser.platform.kotlinmobile.utils.TemplateStr;
import ru.pocketbyte.locolaser.resource.entity.ResItem;
import ru.pocketbyte.locolaser.resource.entity.ResMap;
import ru.pocketbyte.locolaser.resource.file.BaseClassResourceFile;

import java.io.File;
import java.io.IOException;

public class KotlinAndroidResourceFile extends BaseClassResourceFile {

    private static final String CLASS_HEADER_TEMPLATE = "package %1$s\r\n"
            + "\r\n"
            + "import android.content.Context\r\n"
            + "import %3$s.R\r\n"
            + "\r\n"
            + "public class %2$s(private val context: Context) {\r\n";

    private static final String CLASS_HEADER_TEMPLATE_WITH_INTERFACE = "package %1$s\r\n"
            + "\r\n"
            + "import android.content.Context\r\n"
            + "import %3$s.R\r\n"
            + "import %5$s.%4$s\r\n"
            + "\r\n"
            + "public class %2$s(private val context: Context): %4$s {\r\n";

    private static final String CLASS_FOOTER_TEMPLATE = "}";

    private static final String PROPERTY_TEMPLATE =
            "    public val %1$s: String\r\n" +
            "        get() = this.context.getString(R.string.%2$s)\r\n";
    private static final String PROPERTY_TEMPLATE_WITH_OVERRIDE =
            "    override public val %1$s: String\r\n" +
            "        get() = this.context.getString(R.string.%2$s)\r\n";

    private static final int MAX_LINE_SIZE = 120;

    private String mClassName;
    private String mClassPackage;
    private String mInterfaceName;
    private String mInterfacePackage;
    private String mAppPackage;

    public KotlinAndroidResourceFile(File file, String className, String classPackage,
                                     String interfaceName, String interfacePackage, String appPackage) {
        super(file);
        mClassName = className;
        mClassPackage = classPackage;
        mInterfaceName = interfaceName;
        mInterfacePackage = interfacePackage;
        mAppPackage = appPackage;
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
            writeStringLn(String.format(CLASS_HEADER_TEMPLATE_WITH_INTERFACE, mClassPackage, mClassName, mAppPackage,
                    mInterfaceName, mInterfacePackage));
        else
            writeStringLn(String.format(CLASS_HEADER_TEMPLATE, mClassPackage, mClassName, mAppPackage));
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
        if (mInterfaceName != null && mInterfacePackage != null)
            writeStringLn(String.format(PROPERTY_TEMPLATE_WITH_OVERRIDE, propertyName, item.key.trim()));
        else
            writeStringLn(String.format(PROPERTY_TEMPLATE, propertyName, item.key.trim()));
    }

    @Override
    protected void writeClassFooter() throws IOException {
        writeString(CLASS_FOOTER_TEMPLATE);
    }
}