package ru.pocketbyte.locolaser.platform.kotlinmobile.resource;

import org.junit.Test;
import ru.pocketbyte.locolaser.resource.file.ResourceFile;

import java.io.File;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class AbsKotlinPlatformResourcesTest {

    @Test
    public void testConstructor() {
        AbsKotlinPlatformResourcesImpl resources = new AbsKotlinPlatformResourcesImpl(
                new File("./"), "com.package.FileName");

        assertEquals("FileName", resources.className);
        assertEquals("com.package", resources.classPackage);
        assertEquals("com/package", resources.classPackagePath);

        resources = new AbsKotlinPlatformResourcesImpl(
                new File("./"), "ru.pocketbyte.StrClass");

        assertEquals("StrClass", resources.className);
        assertEquals("ru.pocketbyte", resources.classPackage);
        assertEquals("ru/pocketbyte", resources.classPackagePath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidName() {
        new AbsKotlinPlatformResourcesImpl(new File("./"), "ClassName");
    }

    private static class AbsKotlinPlatformResourcesImpl extends AbsKotlinPlatformResources {

        public AbsKotlinPlatformResourcesImpl(File dir, String name) {
            super(dir, name);
        }

        @Override
        protected ResourceFile[] getResourceFiles(Set<String> locales) {
            return null;
        }
    }
}
