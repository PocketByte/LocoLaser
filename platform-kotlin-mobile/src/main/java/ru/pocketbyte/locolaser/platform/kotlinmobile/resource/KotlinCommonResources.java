package ru.pocketbyte.locolaser.platform.kotlinmobile.resource;

import ru.pocketbyte.locolaser.platform.kotlinmobile.resource.file.KotlinCommonResourceFile;
import ru.pocketbyte.locolaser.resource.file.ResourceFile;

import java.io.File;
import java.util.Set;

public class KotlinCommonResources extends AbsKotlinPlatformResources {

    public KotlinCommonResources(File dir, String name) {
        super(dir, name);
    }

    @Override
    protected ResourceFile[] getResourceFiles(Set<String> locales) {
        return new ResourceFile[] {
                new KotlinCommonResourceFile(getFile(), this.className, this.classPackage)
        };
    }
}
