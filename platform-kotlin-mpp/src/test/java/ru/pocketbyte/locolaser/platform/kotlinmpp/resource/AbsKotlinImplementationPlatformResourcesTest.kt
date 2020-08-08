package ru.pocketbyte.locolaser.platform.kotlinmpp.resource

import org.junit.Test
import ru.pocketbyte.locolaser.resource.file.ResourceFile

import java.io.File

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

class AbsKotlinImplementationPlatformResourcesTest {

    @Test
    fun testConstructor() {
        var resources = AbsKotlinPlatformResourcesImpl(
                File("./"), "package.FileName", "com.package.InterfaceName")

        assertEquals("InterfaceName", resources.interfaceName)
        assertEquals("com.package", resources.interfacePackage)

        resources = AbsKotlinPlatformResourcesImpl(
                File("./"), "package.FileName", "ru.pocketbyte.StrInterface")

        assertEquals("StrInterface", resources.interfaceName)
        assertEquals("ru.pocketbyte", resources.interfacePackage)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorInvalidInterfaceName() {
        AbsKotlinPlatformResourcesImpl(File("./"), "package.FileName", "Interface")
    }

    @Test
    fun testConstructorNullInterfaceName() {
        val resources = AbsKotlinPlatformResourcesImpl(
                File("./"), "package.FileName", null)

        assertNull(resources.interfaceName)
        assertNull(resources.interfacePackage)
    }

    private class AbsKotlinPlatformResourcesImpl(
            dir: File,
            name: String,
            interfaceName: String?
    ) : AbsKotlinImplementationPlatformResources(dir, name, interfaceName, null) {

        override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
            return null
        }
    }
}
