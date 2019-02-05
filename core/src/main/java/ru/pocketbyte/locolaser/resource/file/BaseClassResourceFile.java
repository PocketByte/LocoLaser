package ru.pocketbyte.locolaser.resource.file;

import ru.pocketbyte.locolaser.config.WritingConfig;
import ru.pocketbyte.locolaser.resource.PlatformResources;
import ru.pocketbyte.locolaser.resource.entity.*;
import ru.pocketbyte.locolaser.utils.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseClassResourceFile extends ResourceStreamFile {

    protected abstract void writeHeaderComment(ResMap resMap, WritingConfig writingConfig) throws IOException ;
    protected abstract void writeClassHeader(ResMap resMap, WritingConfig writingConfig) throws IOException ;
    protected abstract void writeComment(WritingConfig writingConfig, String comment) throws IOException ;
    protected abstract void writeProperty(WritingConfig writingConfig, String propertyName, ResItem item) throws IOException ;
    protected abstract void writeClassFooter(ResMap resMap, WritingConfig writingConfig) throws IOException ;

    public BaseClassResourceFile(File file) {
        super(file);
    }

    @Override
    public ResMap read() {
        return null;
    }

    @Override
    public void write(ResMap resMap, WritingConfig writingConfig) throws IOException {
        ResLocale locale = resMap.get(PlatformResources.BASE_LOCALE);
        if (locale != null) {
            open();
            writeHeaderComment(resMap, writingConfig);
            writeln();
            writeClassHeader(resMap, writingConfig);

            Set<String> keysSet = new HashSet<>();
            for (ResItem item : locale.values()) {
                String propertyName = TextUtils.keyToProperty(item.key);
                if (!keysSet.contains(propertyName)) {

                    keysSet.add(propertyName);

                    ResValue valueOther = item.valueForQuantity(Quantity.OTHER);
                    if (valueOther != null && valueOther.value != null) {
                        writeComment(writingConfig, valueOther.value);
                    }

                    writeProperty(writingConfig, propertyName, item);
                }
            }

            writeClassFooter(resMap, writingConfig);
            close();
        }
    }
}
