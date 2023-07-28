package ru.pocketbyte.locolaser.kotlinmpp.resource

import org.junit.Test
import ru.pocketbyte.locolaser.resource.file.ResourceFile

import java.io.File

import org.junit.Assert.assertEquals
import ru.pocketbyte.locolaser.kotlinmpp.resource.file.provider.KotlinClassResourceFileProvider
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType

class AbsKotlinResourcesTest {

    @Test
    fun testConstructor() {
        var resources = AbsKotlinResourcesImpl(
                File("./"), "com.package.FileName")

        assertEquals("FileName", resources.className)
        assertEquals("com.package", resources.classPackage)

        resources = AbsKotlinResourcesImpl(
                File("./"), "ru.pocketbyte.StrClass")

        assertEquals("StrClass", resources.className)
        assertEquals("ru.pocketbyte", resources.classPackage)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorInvalidName() {
        AbsKotlinResourcesImpl(File("./"), "ClassName")
    }

    private class AbsKotlinResourcesImpl(
        dir: File, name: String
    ) : AbsKotlinResources(dir, name, KotlinClassResourceFileProvider(), null) {

        override val formattingType: FormattingType = NoFormattingType

        override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
            return null
        }
    }
}
