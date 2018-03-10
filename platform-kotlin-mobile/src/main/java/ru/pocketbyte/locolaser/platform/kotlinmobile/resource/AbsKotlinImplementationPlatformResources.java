package ru.pocketbyte.locolaser.platform.kotlinmobile.resource;

import java.io.File;

public abstract class AbsKotlinImplementationPlatformResources extends AbsKotlinPlatformResources {

    protected final String interfaceName;
    protected final String interfacePackage;

    public AbsKotlinImplementationPlatformResources(File dir, String name, String interfaceName) {
        super(dir, name);

        if (interfaceName != null) {
            String[] interfaceNameParts = interfaceName.split("\\.");

            if (interfaceNameParts.length < 2)
                throw new IllegalArgumentException("Invalid interface name");

            this.interfaceName = interfaceNameParts[interfaceNameParts.length - 1];
            this.interfacePackage = interfaceName.substring(0, interfaceName.length() - this.interfaceName.length() - 1);
        }
        else {
            this.interfaceName = null;
            this.interfacePackage = null;
        }
    }
}
