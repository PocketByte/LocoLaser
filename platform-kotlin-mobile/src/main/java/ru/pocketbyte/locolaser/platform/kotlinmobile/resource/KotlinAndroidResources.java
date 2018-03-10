package ru.pocketbyte.locolaser.platform.kotlinmobile.resource;

import ru.pocketbyte.locolaser.platform.kotlinmobile.resource.file.KotlinAndroidResourceFile;
import ru.pocketbyte.locolaser.resource.file.ResourceFile;

import java.io.File;
import java.util.Set;

public class KotlinAndroidResources extends AbsKotlinImplementationPlatformResources {

    private String mAppPackage;

    public KotlinAndroidResources(File dir, String name, String interfaceName, String appPackage) {
        super(dir, name, interfaceName);
        mAppPackage = appPackage;
    }

    @Override
    protected ResourceFile[] getResourceFiles(Set<String> locales) {
        return new ResourceFile[] {
                new KotlinAndroidResourceFile(getFile(),
                        this.className, this.classPackage,
                        this.interfaceName, this.interfacePackage, mAppPackage)
        };
    }
}
