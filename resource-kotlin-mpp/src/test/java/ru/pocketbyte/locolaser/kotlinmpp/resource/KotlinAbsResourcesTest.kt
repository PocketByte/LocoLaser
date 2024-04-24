package ru.pocketbyte.locolaser.kotlinmpp.resource

import org.junit.Test
import ru.pocketbyte.locolaser.resource.file.ResourceFile

import java.io.File

import org.junit.Assert.assertEquals
import ru.pocketbyte.locolaser.kotlinmpp.resource.file.provider.KotlinClassResourceFileProvider
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType

class KotlinAbsResourcesTest {

    @Test
    fun testConstructor() {
        var resources = KotlinAbsResourcesImpl(
                File("./"), "com.package.FileName")

        assertEquals("FileName", resources.className)
        assertEquals("com.package", resources.classPackage)

        resources = KotlinAbsResourcesImpl(
                File("./"), "ru.pocketbyte.StrClass")

        assertEquals("StrClass", resources.className)
        assertEquals("ru.pocketbyte", resources.classPackage)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorInvalidName() {
        KotlinAbsResourcesImpl(File("./"), "ClassName")
    }

    private class KotlinAbsResourcesImpl(
        dir: File, name: String
    ) : KotlinAbsResources(dir, name, KotlinClassResourceFileProvider, null) {

        override val formattingType: FormattingType = NoFormattingType

        override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
            return null
        }
    }
}
