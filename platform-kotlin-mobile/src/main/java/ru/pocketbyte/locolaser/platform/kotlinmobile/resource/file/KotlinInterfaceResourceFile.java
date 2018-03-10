package ru.pocketbyte.locolaser.platform.kotlinmobile.resource.file;

import org.apache.commons.lang3.text.WordUtils;
import ru.pocketbyte.locolaser.platform.kotlinmobile.utils.TemplateStr;
import ru.pocketbyte.locolaser.resource.entity.*;
import ru.pocketbyte.locolaser.resource.file.BaseClassResourceFile;

import java.io.File;
import java.io.IOException;

public class KotlinInterfaceResourceFile extends BaseClassResourceFile {

    private static final String CLASS_HEADER_TEMPLATE = "package %1$s\r\n"
            + "\r\n"
            + "interface %2$s {\r\n";

    private static final String CLASS_FOOTER_TEMPLATE = "}";

    private static final String PROPERTY_TEMPLATE = "    val %s: String\r\n";

    private static final int MAX_LINE_SIZE = 120;

    private String mClassName;
    private String mClassPackage;

    public KotlinInterfaceResourceFile(File file, String className, String classPackage) {
        super(file);
        mClassName = className;
        mClassPackage = classPackage;
    }

    @Override
    public ResMap read() {
        return null;
    }

    @Override
    protected void writeHeaderComment() throws IOException  {
        writeStringLn(TemplateStr.GENERATED_CLASS_COMMENT);
    }

    @Override
    protected void writeClassHeader() throws IOException {
        writeStringLn(String.format(CLASS_HEADER_TEMPLATE, mClassPackage, mClassName));
    }

    @Override
    protected void writeComment(String comment) throws IOException {
        String commentLinePrefix = "    * ";
        writeStringLn("    /**");
        writeString(commentLinePrefix);
        writeStringLn(WordUtils.wrap(comment, MAX_LINE_SIZE - commentLinePrefix.length()
                , "\r\n" + commentLinePrefix, true));
        writeStringLn("    */");
    }

    @Override
    protected void writeProperty(String propertyName, ResItem item) throws IOException {
        writeStringLn(String.format(PROPERTY_TEMPLATE, propertyName));
    }

    @Override
    protected void writeClassFooter() throws IOException {
        writeString(CLASS_FOOTER_TEMPLATE);
    }
}
