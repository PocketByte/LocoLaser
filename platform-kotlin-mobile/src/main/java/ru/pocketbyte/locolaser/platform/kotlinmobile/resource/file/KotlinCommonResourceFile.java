package ru.pocketbyte.locolaser.platform.kotlinmobile.resource.file;

import org.apache.commons.lang3.text.WordUtils;
import ru.pocketbyte.locolaser.config.WritingConfig;
import ru.pocketbyte.locolaser.platform.kotlinmobile.utils.TemplateStr;
import ru.pocketbyte.locolaser.resource.entity.*;
import ru.pocketbyte.locolaser.resource.file.BaseClassResourceFile;

import java.io.File;
import java.io.IOException;

public class KotlinCommonResourceFile extends BaseClassResourceFile {

    private static final String CLASS_HEADER_TEMPLATE = "package %1$s\r\n"
            + "\r\n"
            + "interface %2$s {\r\n";

    private static final String CLASS_FOOTER_TEMPLATE = "}";

    private static final String PROPERTY_TEMPLATE        = "    val %s: String\r\n";
    private static final String PROPERTY_PLURAL_TEMPLATE = "    fun %s(count: Int): String\r\n";

    private static final int MAX_LINE_SIZE = 120;

    private String mClassName;
    private String mClassPackage;

    public KotlinCommonResourceFile(File file, String className, String classPackage) {
        super(file);
        mClassName = className;
        mClassPackage = classPackage;
    }

    @Override
    public ResMap read() {
        return null;
    }

    @Override
    protected void writeHeaderComment(ResMap resMap, WritingConfig writingConfig) throws IOException  {
        writeStringLn(TemplateStr.GENERATED_CLASS_COMMENT);
    }

    @Override
    protected void writeClassHeader(ResMap resMap, WritingConfig writingConfig) throws IOException {
        writeStringLn(String.format(CLASS_HEADER_TEMPLATE, mClassPackage, mClassName));
    }

    @Override
    protected void writeComment(WritingConfig writingConfig, String comment) throws IOException {
        String commentLinePrefix = "    * ";
        writeStringLn("    /**");
        writeString(commentLinePrefix);
        writeStringLn(WordUtils.wrap(comment, MAX_LINE_SIZE - commentLinePrefix.length()
                , "\r\n" + commentLinePrefix, true));
        writeStringLn("    */");
    }

    @Override
    protected void writeProperty(WritingConfig writingConfig, String propertyName, ResItem item) throws IOException {
        writeStringLn(String.format(
                item.isHasQuantities()
                        ? PROPERTY_PLURAL_TEMPLATE
                        : PROPERTY_TEMPLATE,
                propertyName));
    }

    @Override
    protected void writeClassFooter(ResMap resMap, WritingConfig writingConfig) throws IOException {
        writeString(CLASS_FOOTER_TEMPLATE);
    }
}
