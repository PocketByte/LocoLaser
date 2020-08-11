package ru.pocketbyte.locolaser.platform.kotlinmpp.resource

import org.junit.Test
import ru.pocketbyte.locolaser.resource.file.ResourceFile

import java.io.File

import org.junit.Assert.assertEquals
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType

class AbsKotlinPlatformResourcesTest {

    @Test
    fun testConstructor() {
        var resources = AbsKotlinPlatformResourcesImpl(
                File("./"), "com.package.FileName")

        assertEquals("FileName", resources.className)
        assertEquals("com.package", resources.classPackage)
        assertEquals("com/package", resources.classPackagePath)

        resources = AbsKotlinPlatformResourcesImpl(
                File("./"), "ru.pocketbyte.StrClass")

        assertEquals("StrClass", resources.className)
        assertEquals("ru.pocketbyte", resources.classPackage)
        assertEquals("ru/pocketbyte", resources.classPackagePath)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorInvalidName() {
        AbsKotlinPlatformResourcesImpl(File("./"), "ClassName")
    }

    private class AbsKotlinPlatformResourcesImpl(dir: File, name: String) : AbsKotlinPlatformResources(dir, name, null) {

        override val formattingType: FormattingType = NoFormattingType

        override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
            return null
        }
    }
}
