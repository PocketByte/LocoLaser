package ru.pocketbyte.locolaser.platform.kotlinmobile.resource;

import ru.pocketbyte.locolaser.platform.kotlinmobile.resource.file.KotlinIosResourceFile;
import ru.pocketbyte.locolaser.resource.file.ResourceFile;

import java.io.File;
import java.util.Set;

public class KotlinIosResources extends AbsKotlinImplementationPlatformResources {

    public KotlinIosResources(File dir, String name, String interfaceName) {
        super(dir, name, interfaceName);
    }

    @Override
    protected ResourceFile[] getResourceFiles(Set<String> locales) {
        return new ResourceFile[] {
                new KotlinIosResourceFile(getFile(),
                        this.className, this.classPackage,
                        this.interfaceName, this.interfacePackage)
        };
    }
}
