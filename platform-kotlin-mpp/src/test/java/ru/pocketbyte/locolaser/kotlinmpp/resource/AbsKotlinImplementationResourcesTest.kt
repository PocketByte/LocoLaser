package ru.pocketbyte.locolaser.kotlinmpp.resource

import org.junit.Test
import ru.pocketbyte.locolaser.resource.file.ResourceFile

import java.io.File

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType

class AbsKotlinImplementationResourcesTest {

    @Test
    fun testConstructor() {
        var resources = AbsKotlinResourcesImpl(
                File("./"), "package.FileName", "com.package.InterfaceName")

        assertEquals("InterfaceName", resources.interfaceName)
        assertEquals("com.package", resources.interfacePackage)

        resources = AbsKotlinResourcesImpl(
                File("./"), "package.FileName", "ru.pocketbyte.StrInterface")

        assertEquals("StrInterface", resources.interfaceName)
        assertEquals("ru.pocketbyte", resources.interfacePackage)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorInvalidInterfaceName() {
        AbsKotlinResourcesImpl(File("./"), "package.FileName", "Interface")
    }

    @Test
    fun testConstructorNullInterfaceName() {
        val resources = AbsKotlinResourcesImpl(
                File("./"), "package.FileName", null)

        assertNull(resources.interfaceName)
        assertNull(resources.interfacePackage)
    }

    private class AbsKotlinResourcesImpl(
            dir: File,
            name: String,
            interfaceName: String?
    ) : AbsKotlinImplementationResources(dir, name, interfaceName, null) {

        override val formattingType: FormattingType = NoFormattingType

        override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
            return null
        }
    }
}
