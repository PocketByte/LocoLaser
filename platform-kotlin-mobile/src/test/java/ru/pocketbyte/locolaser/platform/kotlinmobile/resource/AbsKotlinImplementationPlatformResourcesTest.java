package ru.pocketbyte.locolaser.platform.kotlinmobile.resource;

import org.junit.Test;
import ru.pocketbyte.locolaser.resource.file.ResourceFile;

import java.io.File;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AbsKotlinImplementationPlatformResourcesTest {

    @Test
    public void testConstructor() {
        AbsKotlinPlatformResourcesImpl resources = new AbsKotlinPlatformResourcesImpl(
                new File("./"), "package.FileName", "com.package.InterfaceName");

        assertEquals("InterfaceName", resources.interfaceName);
        assertEquals("com.package", resources.interfacePackage);

        resources = new AbsKotlinPlatformResourcesImpl(
                new File("./"), "package.FileName", "ru.pocketbyte.StrInterface");

        assertEquals("StrInterface", resources.interfaceName);
        assertEquals("ru.pocketbyte", resources.interfacePackage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidInterfaceName() {
        new AbsKotlinPlatformResourcesImpl(new File("./"), "package.FileName", "Interface");
    }

    @Test
    public void testConstructorNullInterfaceName() {
        AbsKotlinPlatformResourcesImpl resources = new AbsKotlinPlatformResourcesImpl(
                new File("./"), "package.FileName", null);

        assertNull(resources.interfaceName);
        assertNull(resources.interfacePackage);
    }

    private static class AbsKotlinPlatformResourcesImpl extends AbsKotlinImplementationPlatformResources {

        public AbsKotlinPlatformResourcesImpl(File dir, String name, String interfaceName) {
            super(dir, name, interfaceName);
        }

        @Override
        protected ResourceFile[] getResourceFiles(Set<String> locales) {
            return null;
        }
    }
}
