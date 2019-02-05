package ru.pocketbyte.locolaser.platform.kotlinmobile.resource.file;

import org.apache.commons.lang3.text.WordUtils;
import ru.pocketbyte.locolaser.config.WritingConfig;
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
            + "import %4$s.%5$s\r\n"
            + "\r\n"
            + "public class %2$s(private val context: Context): %5$s {\r\n";

    private static final String CLASS_FOOTER_TEMPLATE = "}";

    private static final String PROPERTY_TEMPLATE =
            "    %1$spublic val %2$s: String\r\n" +
            "        get() = this.context.getString(R.string.%3$s)\r\n";

    private static final String PROPERTY_PLURAL_TEMPLATE =
            "    %1$spublic fun %2$s(count: Int): String {\r\n" +
            "        return this.context.getString(R.plurals.%3$s, count)\r\n" +
            "    }\r\n";

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
    protected void writeHeaderComment(ResMap resMap, WritingConfig writingConfig) throws IOException {
        writeStringLn(TemplateStr.GENERATED_CLASS_COMMENT);
    }

    @Override
    protected void writeClassHeader(ResMap resMap, WritingConfig writingConfig) throws IOException {
        if (mInterfaceName != null && mInterfacePackage != null)
            writeStringLn(String.format(CLASS_HEADER_TEMPLATE_WITH_INTERFACE,
                    mClassPackage, mClassName, mAppPackage,
                    mInterfacePackage, mInterfaceName));
        else
            writeStringLn(String.format(CLASS_HEADER_TEMPLATE,
                    mClassPackage, mClassName, mAppPackage));
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
}