package ru.pocketbyte.locolaser.platform.kotlinmobile.resource;

import ru.pocketbyte.locolaser.platform.kotlinmobile.resource.file.KotlinInterfaceResourceFile;
import ru.pocketbyte.locolaser.resource.file.ResourceFile;

import java.io.File;
import java.util.Set;

public class KotlinInterfaceResources extends AbsKotlinPlatformResources {

    public KotlinInterfaceResources(File dir, String name) {
        super(dir, name);
    }

    @Override
    protected ResourceFile[] getResourceFiles(Set<String> locales) {
        return new ResourceFile[] {
                new KotlinInterfaceResourceFile(getFile(), this.className, this.classPackage)
        };
    }
}
